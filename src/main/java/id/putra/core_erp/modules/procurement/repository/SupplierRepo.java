package id.putra.core_erp.modules.procurement.repository;

import java.util.List;
import java.util.Optional;

import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.enums.Status;
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

    public List<Supplier> findByStatus(Status status) {
        return list("status", status);
    }

    //
    // findAllByNameContainingIgnoreCaseAndStatusStartingWithIgnoreCase(String name,
    // String status,
    // Pageable pageable);
}
