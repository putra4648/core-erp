package id.putra.core_erp.modules.inbound.repository;

import id.putra.core_erp.modules.inbound.model.PurchaseOrder;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PurchaseOrderRepository implements PanacheRepository<PurchaseOrder> {

}
