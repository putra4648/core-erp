package id.putra.core_erp.modules.inbound.repository;

import id.putra.core_erp.modules.procurement.model.PurchaseOrder;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PurchaseOrderRepository implements SoftDeleteRepository<PurchaseOrder> {

	@Inject
	PurchaseOrderLineRepository purchaseOrderLineRepository;

	/** PO lines belong to the PO aggregate — flag them with the header. */
	@Override
	public void cascadeToChildren(PurchaseOrder purchaseOrder) {
		purchaseOrderLineRepository.find("purchaseOrder.id", purchaseOrder.id).list()
				.forEach(purchaseOrderLineRepository::softDelete);
	}

}
