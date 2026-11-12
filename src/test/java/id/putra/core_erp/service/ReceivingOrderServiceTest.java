package id.putra.core_erp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import id.putra.core_erp.enums.OrderStatus;
import id.putra.core_erp.modules.inbound.model.Receiving;
import id.putra.core_erp.modules.inbound.repository.ReceivingLineRepository;
import id.putra.core_erp.modules.inbound.repository.ReceivingRepository;
import id.putra.core_erp.modules.inbound.service.ReceivingOrderService;
import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.modules.product.repository.ProductRepository;
import id.putra.core_erp.modules.procurement.model.PurchaseOrder;
import id.putra.core_erp.modules.procurement.model.PurchaseOrderLine;
import id.putra.core_erp.modules.inbound.repository.PurchaseOrderRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers {@link ReceivingOrderService} - the goods-receipt step of the P2P
 * flow.
 *
 * <p>
 * Two rules carry real consequences:
 *
 * <ul>
 * <li>Completing a receipt <em>accumulates</em> received quantities onto the PO
 * lines, so
 * it must support partial deliveries across several receipts rather than
 * overwriting.
 * An overwrite would silently lose every earlier delivery.</li>
 * <li>The PO only moves to {@code COMPLETED} when <em>every</em> line is
 * satisfied.
 * Closing early makes the remainder undeliverable.</li>
 * </ul>
 */
@QuarkusTest
class ReceivingOrderServiceTest {

	@Inject
	ReceivingOrderService receivingOrderService;

	@Inject
	ReceivingRepository receivingRepository;

	@Inject
	ReceivingLineRepository receivingLineRepository;

	@Inject
	PurchaseOrderRepository purchaseOrderRepository;

	@Inject
	ProductRepository productRepository;

	@Inject
	EntityManager em;

	private static final AtomicInteger SEQ = new AtomicInteger();

	private Product newProduct() {
		Product p = new Product();
		p.sku = "RCP-SKU-" + SEQ.incrementAndGet();
		p.name = "Receiving Product " + SEQ.get();
		p.quantity = 0;
		p.minStock = 1;
		p.maxStock = 100;
		p.price = new BigDecimal("9.9900");
		productRepository.persist(p);
		return p;
	}

	/** Persists a PO with one line, then reloads it managed. */
	private PurchaseOrder newPurchaseOrder(String poNumber, OrderStatus status, Product product, int orderedQty) {
		PurchaseOrder po = new PurchaseOrder();
		po.poNumber = poNumber;
		po.status = status;
		purchaseOrderRepository.persist(po);

		PurchaseOrderLine line = new PurchaseOrderLine();
		line.setProduct(product);
		line.setQuantity(orderedQty);
		line.setQtyReceived(0);
		line.setPurchaseOrder(po);
		em.persist(line);

		em.flush();
		em.clear();
		return purchaseOrderRepository.find("poNumber", poNumber).firstResult();
	}

	/** Marks the receipt's line with what the dock actually counted. */
	private void recordReceived(Receiving receiving, String qty) {
		var line = receiving.receivingLines.get(0);
		line.qtyReceived = new BigDecimal(qty);
		receivingLineRepository.getEntityManager().merge(line);
		em.flush();
	}

	// ------------------------------------------------- generateFromPurchaseOrder

	@Test
	@TestTransaction
	@DisplayName("generateFromPurchaseOrder rejects a null id")
	void generateRejectsNullId() {
		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> receivingOrderService.generateFromPurchaseOrder(null));

		assertTrue(ex.getMessage().contains("id is required"), "got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("generateFromPurchaseOrder rejects an unknown purchase order")
	void generateRejectsUnknownPurchaseOrder() {
		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> receivingOrderService.generateFromPurchaseOrder(999_999L));

		assertTrue(ex.getMessage().contains("Purchase Order not found"), "got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("generate copies every PO line and numbers the receipt REC-<poNumber>")
	void generateCopiesLinesAndNumbersReceipt() {
		Product product = newProduct();
		PurchaseOrder po = newPurchaseOrder("PO-GEN-1", OrderStatus.PENDING, product, 25);

		Receiving receiving = receivingOrderService.generateFromPurchaseOrder(po.id);
		em.flush();
		em.clear();

		Receiving reloaded = receivingRepository.findById(receiving.id);

		assertNotNull(reloaded, "the receipt must be persisted");
		assertEquals("REC-PO-GEN-1", reloaded.receivingNumber,
				"the receipt number must derive from the PO number");
		assertEquals(OrderStatus.PENDING, reloaded.status, "a new receipt starts PENDING");
		assertEquals(1, reloaded.receivingLines.size(), "every PO line must be copied");

		var line = reloaded.receivingLines.get(0);
		assertEquals(0, new BigDecimal("25").compareTo(line.qty), "the ordered quantity must carry over");
		assertEquals(0, BigDecimal.ZERO.compareTo(line.qtyReceived),
				"nothing is received yet - the dock fills this in");
		assertEquals("PENDING_QC", line.status);
		assertNotNull(line.purchaseOrderLine, "the line must link back to its PO line for traceability");
	}

	@Test
	@TestTransaction
	@DisplayName("generate advances a DRAFT purchase order to PENDING")
	void generateAdvancesDraftPurchaseOrder() {
		Product product = newProduct();
		PurchaseOrder po = newPurchaseOrder("PO-GEN-2", OrderStatus.DRAFT, product, 10);

		receivingOrderService.generateFromPurchaseOrder(po.id);
		em.flush();
		em.clear();

		assertEquals(OrderStatus.PENDING,
				purchaseOrderRepository.find("poNumber", "PO-GEN-2").firstResult().status,
				"receiving against a draft must move it forward to PENDING");
	}

	@Test
	@TestTransaction
	@DisplayName("generate leaves a purchase order already past draft alone")
	void generateDoesNotRewriteExistingStatus() {
		Product product = newProduct();
		PurchaseOrder po = newPurchaseOrder("PO-GEN-3", OrderStatus.APPROVED, product, 10);

		receivingOrderService.generateFromPurchaseOrder(po.id);
		em.flush();
		em.clear();

		assertEquals(OrderStatus.APPROVED,
				purchaseOrderRepository.find("poNumber", "PO-GEN-3").firstResult().status,
				"a PO past draft must not be reset by generating another receipt");
	}

	// --------------------------------------------------------- completeReceiving

	@Test
	@TestTransaction
	@DisplayName("completeReceiving rejects an unknown receipt")
	void completeRejectsUnknownReceipt() {
		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> receivingOrderService.completeReceiving(999_999L));

		assertTrue(ex.getMessage().contains("Receiving not found"), "got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("completing an already-completed receipt is refused")
	void completeRejectsAlreadyCompleted() {
		Product product = newProduct();
		PurchaseOrder po = newPurchaseOrder("PO-CMP-1", OrderStatus.PENDING, product, 10);
		Receiving receiving = receivingOrderService.generateFromPurchaseOrder(po.id);
		em.flush();

		receivingOrderService.completeReceiving(receiving.id);
		em.flush();

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> receivingOrderService.completeReceiving(receiving.id));

		assertTrue(ex.getMessage().contains("already completed"),
				"a double completion would double-count the received quantity, got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("completing a receipt rolls the received quantity onto the PO line")
	void completeRollsUpQuantity() {
		Product product = newProduct();
		PurchaseOrder po = newPurchaseOrder("PO-CMP-2", OrderStatus.PENDING, product, 10);
		Receiving receiving = receivingOrderService.generateFromPurchaseOrder(po.id);
		recordReceived(receiving, "4");

		receivingOrderService.completeReceiving(receiving.id);
		em.flush();
		em.clear();

		PurchaseOrderLine poLine = purchaseOrderRepository.find("poNumber", "PO-CMP-2").firstResult().purchaseOrderLines
				.get(0);

		assertEquals(4, poLine.getQtyReceived(), "the received quantity must roll up onto the PO line");
	}

	@Test
	@TestTransaction
	@DisplayName("the PO stays PENDING while any line is short")
	void partialDeliveryKeepsPurchaseOrderPending() {
		Product product = newProduct();
		PurchaseOrder po = newPurchaseOrder("PO-CMP-3", OrderStatus.PENDING, product, 10);
		Receiving receiving = receivingOrderService.generateFromPurchaseOrder(po.id);
		recordReceived(receiving, "4"); // short of the 10 ordered

		receivingOrderService.completeReceiving(receiving.id);
		em.flush();
		em.clear();

		assertEquals(OrderStatus.PENDING,
				purchaseOrderRepository.find("poNumber", "PO-CMP-3").firstResult().status,
				"a partial delivery must not close the PO - the remainder is still expected");
		assertEquals(OrderStatus.COMPLETED, receivingRepository.findById(receiving.id).status,
				"the receipt itself is still completed");
	}

	@Test
	@TestTransaction
	@DisplayName("the PO completes once every line is fully received")
	void fullDeliveryCompletesPurchaseOrder() {
		Product product = newProduct();
		PurchaseOrder po = newPurchaseOrder("PO-CMP-4", OrderStatus.PENDING, product, 10);
		Receiving receiving = receivingOrderService.generateFromPurchaseOrder(po.id);
		recordReceived(receiving, "10");

		receivingOrderService.completeReceiving(receiving.id);
		em.flush();
		em.clear();

		assertEquals(OrderStatus.COMPLETED,
				purchaseOrderRepository.find("poNumber", "PO-CMP-4").firstResult().status,
				"a fully delivered PO must be marked COMPLETED");
	}

	@Test
	@TestTransaction
	@DisplayName("quantities accumulate across successive receipts rather than overwriting")
	void quantitiesAccumulateAcrossReceipts() {
		Product product = newProduct();
		PurchaseOrder po = newPurchaseOrder("PO-CMP-5", OrderStatus.PENDING, product, 10);

		// First delivery: 4 of 10.
		Receiving first = receivingOrderService.generateFromPurchaseOrder(po.id);
		recordReceived(first, "4");
		receivingOrderService.completeReceiving(first.id);
		em.flush();
		em.clear();

		// Second delivery: 3 more.
		Receiving second = receivingOrderService.generateFromPurchaseOrder(po.id);
		recordReceived(second, "3");
		receivingOrderService.completeReceiving(second.id);
		em.flush();
		em.clear();

		PurchaseOrderLine poLine = purchaseOrderRepository.find("poNumber", "PO-CMP-5").firstResult().purchaseOrderLines
				.get(0);

		assertEquals(7, poLine.getQtyReceived(),
				"the second receipt must ADD to the first (4 + 3 = 7), not overwrite it with 3");
		assertEquals(OrderStatus.PENDING,
				purchaseOrderRepository.find("poNumber", "PO-CMP-5").firstResult().status,
				"7 of 10 is still short, so the PO stays PENDING");
	}
}
