package id.putra.core_erp.purchase_requisition.repository;

import jakarta.enterprise.context.ApplicationScoped;
import id.putra.core_erp.purchase_requisition.model.Inventory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class InventoryRepo implements PanacheRepository<Inventory> {

}
