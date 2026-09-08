package id.putra.core_erp.purchase_requisition.repository;

import java.util.List;
import java.util.Optional;

import id.putra.core_erp.shared.entity.Supplier;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SupplierRepo implements PanacheRepository<Supplier> {

    public List<Supplier> findAllByStatus(String status) {
        return list("status", status);
    }

    public Optional<Supplier> findByAccNo(String accNo) {
        return find("accNo = ?1", accNo).singleResultOptional();
    }

    public int countByNameContainingIgnoreCase(String name) {
        return (int) count("name ilike ?1", "%" + name + "%");
    }

    //
    // findAllByNameContainingIgnoreCaseAndStatusStartingWithIgnoreCase(String name,
    // String status,
    // Pageable pageable);
}
