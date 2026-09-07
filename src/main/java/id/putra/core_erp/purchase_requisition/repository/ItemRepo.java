package id.putra.core_erp.purchase_requisition.repository;

import java.util.Optional;

import id.putra.core_erp.purchase_requisition.model.Item;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ItemRepo implements PanacheRepository<Item> {

    // public
    // findByNameContainingIgnoreCaseAndStatusContainingIgnoreCaseOrSupplierEquals(
    // String name,
    // String status,
    // Supplier supplier,
    // Pageable pageable) {
    // // This query is complex and might need to be handled differently in Panache
    // if
    // // not straightforward.
    // // However, for now, we will use the find method with the correct parameters.
    // // Since Pageable is a Spring Data class, we might need to adapt this.
    // // For now, I will leave it as is or provide a comment as it requires
    // // significant refactoring.
    // return null;
    // }

    public long countByNameContainingIgnoreCase(String name) {
        return count("name ilike ?1", name);
    }

    public Optional<Item> findByNameEqualsIgnoreCase(String name) {
        return find("name ilike ?1", name).firstResultOptional();
    }

}
