package id.putra.core_erp.modules.inbound.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import id.putra.core_erp.enums.OrderStatus;
import id.putra.core_erp.modules.inbound.model.Receiving;
import id.putra.core_erp.modules.inbound.model.ReceivingLine;
import id.putra.core_erp.modules.inbound.repository.PurchaseOrderRepository;
import id.putra.core_erp.modules.inbound.repository.ReceivingLineRepository;
import id.putra.core_erp.modules.inbound.repository.ReceivingRepository;
import id.putra.core_erp.modules.procurement.model.PurchaseOrder;
import id.putra.core_erp.modules.procurement.model.PurchaseOrderLine;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ReceivingOrderService implements BaseService<Receiving> {

	private final ReceivingRepository receivingRepository;
	private final PurchaseOrderRepository purchaseOrderRepository;
	private final ReceivingLineRepository receivingLineRepository;

	public ReceivingOrderService(ReceivingRepository receivingRepository,
			PurchaseOrderRepository purchaseOrderRepository, ReceivingLineRepository receivingLineRepository) {
		this.receivingRepository = receivingRepository;
		this.purchaseOrderRepository = purchaseOrderRepository;
		this.receivingLineRepository = receivingLineRepository;
	}

	@Override
	public void save(Receiving entity) {
		receivingRepository.persist(entity);
	}

	@Override
	public Receiving findById(Long id) {
		return receivingRepository.findById(id);
	}

	@Override
	public void delete(Long id) {
		receivingRepository.softDeleteById(id);
	}

	@Override
	public void update(Receiving entity) {
		receivingRepository.getEntityManager().merge(entity);
	}

	@Override
	public List<Receiving> findAll(Receiving entity, Page page) {
		return receivingRepository.findAll().page(page).list();
	}

	/**
	 * Generates a Goods Receiving (with lines) from a {@link PurchaseOrder}
	 * (P2P step: "Goods Receipt against PO").
	 * <p>
	 * A new {@link Receiving} is created in {@link OrderStatus#PENDING} with
	 * {@code receivingNumber = "REC-" + poNumber}. Each {@code PurchaseOrderLine}
	 * is copied into a {@link ReceivingLine} with the ordered quantity
	 * ({@code qty}), {@code qtyReceived = 0} (filled at the dock), status
	 * {@code PENDING_QC}, and a back-reference to the source PO line for full
	 * traceability. The PO status is advanced to {@link OrderStatus#PENDING} if
	 * it was still a draft.
	 *
	 * @param poId the source purchase order id
	 * @return the persisted receiving document
	 */
	public Receiving generateFromPurchaseOrder(Long poId) {
		if (poId == null) {
			throw new RuntimeException("Purchase Order id is required");
		}
		PurchaseOrder po = purchaseOrderRepository.findById(poId);
		if (po == null) {
			throw new RuntimeException("Purchase Order not found");
		}
		if (po.purchaseOrderLines == null || po.purchaseOrderLines.isEmpty()) {
			throw new RuntimeException("Purchase Order has no lines to receive");
		}

		Receiving receiving = new Receiving();
		receiving.purchaseOrder = po;
		receiving.receivedDate = LocalDateTime.now();
		receiving.receivingNumber = "REC-" + (po.poNumber != null ? po.poNumber : po.id);
		receiving.status = OrderStatus.PENDING;
		receiving.receivingLines = new ArrayList<>();

		for (PurchaseOrderLine poLine : po.purchaseOrderLines) {
			ReceivingLine line = new ReceivingLine();
			line.product = poLine.getProduct();
			line.qty = poLine.getQuantity() != null ? BigDecimal.valueOf(poLine.getQuantity()) : BigDecimal.ZERO;
			line.qtyReceived = BigDecimal.ZERO;
			line.status = "PENDING_QC";
			line.receivedDate = LocalDateTime.now();
			line.purchaseOrderLine = poLine;
			line.receiving = receiving;
			receiving.receivingLines.add(line);
		}

		receivingRepository.persist(receiving);
		// persist lines explicitly to guarantee the FK + purchase_order_line link
		for (ReceivingLine line : receiving.receivingLines) {
			receivingLineRepository.persist(line);
		}

		if (po.status == null || po.status == OrderStatus.DRAFT) {
			po.status = OrderStatus.PENDING;
		}

		return receiving;
	}

	/**
	 * Marks a receiving document as completed and rolls up the received
	 * quantities onto the source {@link PurchaseOrderLine}s (P2P step: "Complete
	 * Goods Receipt"). Each receiving line's {@code qtyReceived} is accumulated
	 * onto the linked PO line, supporting partial deliveries across multiple
	 * receipts. The PO advances to {@link OrderStatus#COMPLETED} only when every
	 * line is fully fulfilled, otherwise it stays {@link OrderStatus#PENDING}.
	 *
	 * @param receivingId the receiving document to complete
	 */
	public void completeReceiving(Long receivingId) {
		Receiving receiving = receivingRepository.findById(receivingId);
		if (receiving == null) {
			throw new RuntimeException("Receiving not found");
		}
		if (receiving.status == OrderStatus.COMPLETED) {
			throw new RuntimeException("Receiving is already completed");
		}

		PurchaseOrder po = receiving.purchaseOrder;

		if (receiving.receivingLines != null) {
			for (ReceivingLine line : receiving.receivingLines) {
				if (line.purchaseOrderLine != null && line.qtyReceived != null) {
					PurchaseOrderLine poLine = line.purchaseOrderLine;
					int current = poLine.getQtyReceived() != null ? poLine.getQtyReceived() : 0;
					poLine.setQtyReceived(current + line.qtyReceived.intValue());
				}
			}
		}

		if (po != null && po.purchaseOrderLines != null) {
			boolean allComplete = true;
			for (PurchaseOrderLine poLine : po.purchaseOrderLines) {
				int received = poLine.getQtyReceived() != null ? poLine.getQtyReceived() : 0;
				int ordered = poLine.getQuantity() != null ? poLine.getQuantity() : 0;
				if (received < ordered) {
					allComplete = false;
					break;
				}
			}
			po.status = allComplete ? OrderStatus.COMPLETED : OrderStatus.PENDING;
		}

		receiving.status = OrderStatus.COMPLETED;
	}

	/**
	 * Persists changes to a single receiving line (e.g. dock-entered received
	 * quantity, lot/batch, expiry). Used by the Receiving UI after inline editing.
	 *
	 * @param line the receiving line to merge
	 */
	public void updateLine(ReceivingLine line) {
		receivingLineRepository.getEntityManager().merge(line);
	}

}
