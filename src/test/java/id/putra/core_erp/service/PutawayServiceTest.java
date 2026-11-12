package id.putra.core_erp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import id.putra.core_erp.modules.inbound.model.ReceivingLine;
import id.putra.core_erp.modules.inbound.repository.PutawayRepository;
import id.putra.core_erp.modules.inbound.repository.ReceivingLineRepository;
import id.putra.core_erp.modules.inbound.service.PutawayService;
import id.putra.core_erp.modules.inventory.model.InventoryItem;
import id.putra.core_erp.modules.inventory.repository.InventoryItemRepository;
import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.modules.product.repository.ProductRepository;
import id.putra.core_erp.modules.warehouse.model.Warehouse;
import id.putra.core_erp.modules.warehouse.repository.WarehouseRepository;
import id.putra.core_erp.support.TestFixtures;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers {@link PutawayService} - the step that increases on-hand stock.
 *
 * <p>
 * The quantity arithmetic is what matters. Putaway finds an existing
 * {@link InventoryItem} for the product + lot and <em>adds</em> to it. Getting
 * that wrong
 * makes the inventory balance drift away from what was physically received, and
 * every
 * downstream report inherits the error. Lot matching is the other trap: two
 * lots of the
 * same product must remain separate rows.
 */
@QuarkusTest
class PutawayServiceTest {

	@Inject
	PutawayService putawayService;

	@Inject
	PutawayRepository putawayRepository;

	@Inject
	ReceivingLineRepository receivingLineRepository;

	@Inject
	InventoryItemRepository inventoryItemRepository;

	@Inject
	WarehouseRepository warehouseRepository;

	@Inject
	ProductRepository productRepository;

	@Inject
	EntityManager em;

	private static final AtomicInteger SEQ = new AtomicInteger();

	private Product newProduct() {
		Product p = new Product();
		p.sku = "PUT-SKU-" + SEQ.incrementAndGet();
		p.name = "Putaway Product " + SEQ.get();
		p.quantity = 0;
		p.minStock = 1;
		p.maxStock = 1000;
		p.price = new BigDecimal("5.0000");
		productRepository.persist(p);
		return p;
	}

	private Warehouse newWarehouse() {
		Warehouse w = TestFixtures.warehouse("WH-PUT-" + SEQ.incrementAndGet());
		warehouseRepository.persist(w);
		return w;
	}

	/**
	 * A receiving line with the given received quantity and lot, ready to put away.
	 */
	private ReceivingLine newReceivingLine(Product product, String qtyReceived, String lot) {
		ReceivingLine line = new ReceivingLine();
		line.product = product;
		line.qty = new BigDecimal("100");
		line.qtyReceived = qtyReceived == null ? null : new BigDecimal(qtyReceived);
		line.lotBatch = lot;
		line.status = "PENDING_QC";
		line.receivedDate = LocalDateTime.now();
		receivingLineRepository.persist(line);
		em.flush();
		return line;
	}

	// --------------------------------------------------------------- guards

	@Test
	@TestTransaction
	@DisplayName("putaway is refused when the receiving line does not exist")
	void rejectsUnknownReceivingLine() {
		Warehouse warehouse = newWarehouse();

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> putawayService.executePutaway(999_999L, warehouse.id));

		assertTrue(ex.getMessage().contains("Receiving line not found"), "got: " + ex.getMessage());
	}

	@Test
	@TestTransaction

	@DisplayName("putaway is refused when no quantity was received")
	void rejectsZeroReceivedQuantity() {
		Product product = newProduct();
		Warehouse warehouse = newWarehouse();
		ReceivingLine line = newReceivingLine(product, "0", "LOT-ZERO");

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> putawayService.executePutaway(line.id, warehouse.id));

		assertTrue(ex.getMessage().contains("no quantity received"),
				"putting away nothing would create a phantom inventory row, got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("putaway is refused when the received quantity is null")
	void rejectsNullReceivedQuantity() {
		Product product = newProduct();
		Warehouse warehouse = newWarehouse();
		ReceivingLine line = newReceivingLine(product, null, "LOT-NULL");

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> putawayService.executePutaway(line.id, warehouse.id));

		assertTrue(ex.getMessage().contains("no quantity received"), "got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("putaway is refused when the warehouse does not exist")
	void rejectsUnknownWarehouse() {
		Product product = newProduct();
		ReceivingLine line = newReceivingLine(product, "5", "LOT-NOWH");

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> putawayService.executePutaway(line.id, 999_999L));

		assertTrue(ex.getMessage().contains("Warehouse not found"), "got: " + ex.getMessage());
	}

	// ------------------------------------------------------------ behaviour

	@Test
	@TestTransaction
	@DisplayName("putaway creates the inventory balance and marks the line PUTAWAY")
	void createsInventoryAndMarksLine() {
		Product product = newProduct();
		Warehouse warehouse = newWarehouse();
		ReceivingLine line = newReceivingLine(product, "7", "LOT-NEW");

		putawayService.executePutaway(line.id, warehouse.id);
		em.flush();
		em.clear();

		var items = inventoryItemRepository.find("product.id", product.id).list();
		assertEquals(1, items.size(), "one inventory row must be created for the product + lot");
		assertEquals(7L, items.get(0).quantity, "the balance must equal the received quantity");
		assertEquals("LOT-NEW", items.get(0).lotNumber);
		assertEquals(0L, items.get(0).reservedQty, "nothing is reserved on creation");

		assertEquals("PUTAWAY", receivingLineRepository.findById(line.id).status,
				"the line must be marked PUTAWAY so it is not processed twice");
	}

	@Test
	@TestTransaction
	@DisplayName("a second putaway of the same product and lot adds to the existing balance")
	void secondPutawayAccumulatesQuantity() {
		Product product = newProduct();
		Warehouse warehouse = newWarehouse();

		putawayService.executePutaway(newReceivingLine(product, "5", "LOT-ACC").id, warehouse.id);
		em.flush();
		em.clear();

		putawayService.executePutaway(newReceivingLine(product, "3", "LOT-ACC").id, warehouse.id);
		em.flush();
		em.clear();

		var items = inventoryItemRepository.find("product.id", product.id).list();
		assertEquals(1, items.size(), "the same product + lot must reuse the existing row");
		assertEquals(8L, items.get(0).quantity,
				"the second putaway must ADD (5 + 3 = 8), not replace the balance with 3");
	}

	@Test
	@TestTransaction
	@DisplayName("different lots of the same product stay separate inventory rows")
	void differentLotsStaySeparate() {
		Product product = newProduct();
		Warehouse warehouse = newWarehouse();

		putawayService.executePutaway(newReceivingLine(product, "5", "LOT-A").id, warehouse.id);
		putawayService.executePutaway(newReceivingLine(product, "9", "LOT-B").id, warehouse.id);
		em.flush();
		em.clear();

		var items = inventoryItemRepository.find("product.id", product.id).list();
		assertEquals(2, items.size(),
				"two lots must NOT be merged - merging would destroy lot traceability on recall");
		assertEquals(14L, items.stream().mapToLong(i -> i.quantity).sum(),
				"but the combined balance must still be right");
	}

	@Test
	@TestTransaction
	@DisplayName("the putaway record captures the product, warehouse and lot")
	void putawayRecordCapturesContext() {
		Product product = newProduct();
		Warehouse warehouse = newWarehouse();
		ReceivingLine line = newReceivingLine(product, "6", "LOT-REC");

		putawayService.executePutaway(line.id, warehouse.id);
		em.flush();
		em.clear();

		var records = putawayRepository.find("receivingLine.id", line.id).list();
		assertEquals(1, records.size(), "exactly one putaway row must be written per line");

		var record = records.get(0);
		assertEquals("DONE", record.status);
		assertEquals("LOT-REC", record.lotNumber);
		assertEquals(product.sku, record.sku, "the SKU must be denormalised onto the record");
		assertNotNull(record.warehouse, "the target warehouse must be recorded");
	}

	@Test
	@TestTransaction
	@DisplayName("a receiving line with no product still putaways without an inventory row")
	void lineWithoutProductSkipsInventory() {
		Warehouse warehouse = newWarehouse();
		ReceivingLine line = newReceivingLine(null, "4", "LOT-NOPROD");

		putawayService.executePutaway(line.id, warehouse.id);
		em.flush();
		em.clear();

		assertEquals("PUTAWAY", receivingLineRepository.findById(line.id).status,
				"the line must still be marked, otherwise it can never be reprocessed");
		assertEquals(1, putawayRepository.find("receivingLine.id", line.id).count(),
				"a putaway record must still be written even without a product");
	}
}
