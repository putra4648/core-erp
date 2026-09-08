package id.putra.core_erp.purchase_requisition.repository;

import java.util.Optional;

import id.putra.core_erp.purchase_requisition.model.Inventory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryRepo implements PanacheRepository<Inventory> {
    public Optional<Inventory> findByItemNumber(String itemNumber) {
        return find("itemNumber = ?1", itemNumber).firstResultOptional();
    }

    public Inventory findByPartNumber(String partNumber) {
        return find("partNumber = ?1", partNumber).firstResult();
    }
}
