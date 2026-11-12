package id.putra.core_erp.modules.inbound.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import id.putra.core_erp.enums.OrderStatus;
import id.putra.core_erp.modules.inbound.repository.PurchaseOrderLineRepository;
import id.putra.core_erp.modules.inbound.repository.PurchaseOrderRepository;
import id.putra.core_erp.modules.procurement.model.PurchaseOrder;
import id.putra.core_erp.modules.procurement.model.PurchaseOrderLine;
import id.putra.core_erp.modules.procurement.model.Quotation;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PurchaseOrderService implements BaseService<PurchaseOrder> {
	private final PurchaseOrderRepository purchaseOrderRepository;
	private final PurchaseOrderLineRepository purchaseOrderLineRepository;

	public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository,
			PurchaseOrderLineRepository purchaseOrderLineRepository) {
		this.purchaseOrderRepository = purchaseOrderRepository;
		this.purchaseOrderLineRepository = purchaseOrderLineRepository;
	}

	@Override
	public void save(PurchaseOrder entity) {
		purchaseOrderRepository.persist(entity);
	}

	@Override
	public void update(PurchaseOrder dto) {
		purchaseOrderRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		purchaseOrderRepository.softDeleteById(id);
	}

	@Override
	public PurchaseOrder findById(Long id) {
		return purchaseOrderRepository.findById(id);
	}

	@Override
	public List<PurchaseOrder> findAll(PurchaseOrder po, Page p) {
		return purchaseOrderRepository.findAll().page(p).list();
	}

	/**
	 * Generates a Purchase Order (with lines) from an approved {@link Quotation}.
	 * <p>
	 * This implements the {@code Quotation -> Purchase Order} step of the
	 * Procure-to-Pay flow:
	 * <ul>
	 * <li>A new {@link PurchaseOrder} is created in {@link OrderStatus#DRAFT}
	 * with the quotation's selected (winning) supplier and due date as the
	 * delivery date.</li>
	 * <li>Each {@code QuotationLine} is copied into a {@link PurchaseOrderLine}
	 * (product, quantity, quoted unit price) and linked back to its source
	 * quotation line for full traceability.</li>
	 * <li>The PO is linked to the quotation (both sides of the bidirectional
	 * relation are set).</li>
	 * </ul>
	 * The caller is responsible for persisting the quotation side of the link
	 * and advancing the source requisition status.
	 *
	 * @param quotation the approved quotation with a selected supplier
	 * @return the persisted purchase order
	 */
	public PurchaseOrder generateFromQuotation(Quotation quotation) {
		if (quotation == null) {
			throw new RuntimeException("Quotation is required");
		}
		if (quotation.selectedSupplier == null) {
			throw new RuntimeException("Cannot generate PO: no winning supplier selected for quotation");
		}

		PurchaseOrder po = new PurchaseOrder();
		po.poNumber = "PO-" + (quotation.rfqNumber != null ? quotation.rfqNumber : quotation.id);
		po.status = OrderStatus.DRAFT;
		po.orderDate = LocalDateTime.now();
		po.deliveryDate = quotation.dueDate;
		po.supplier = quotation.selectedSupplier;
		po.purchaseOrderLines = new ArrayList<>();

		if (quotation.quotationLines != null) {
			for (var ql : quotation.quotationLines) {
				var line = new PurchaseOrderLine();
				line.setProduct(ql.product);
				line.setQuantity(ql.quantity);
				line.setPrice(ql.unitPrice);
				line.setQuotationLine(ql);
				line.setPurchaseOrder(po);
				po.purchaseOrderLines.add(line);
			}
		}

		purchaseOrderRepository.persist(po);

		// persist lines explicitly to guarantee the FK + quotation_line link
		for (var line : po.purchaseOrderLines) {
			purchaseOrderLineRepository.persist(line);
		}

		// link both sides of the bidirectional Quotation <-> PurchaseOrder relation
		po.quotation = quotation;
		quotation.purchaseOrder = po;

		return po;
	}

	@Override
	public long count() {
		return purchaseOrderRepository.count();
	}
}
