package id.putra.core_erp.modules.inbound.repository;

import id.putra.core_erp.modules.procurement.model.PurchaseOrderLine;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PurchaseOrderLineRepository implements PanacheRepository<PurchaseOrderLine> {

}