package id.putra.core_erp.modules.inbound.repository;

import id.putra.core_erp.modules.procurement.model.PurchaseOrderLine;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PurchaseOrderLineRepository implements SoftDeleteRepository<PurchaseOrderLine> {

}