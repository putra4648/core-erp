package id.putra.core_erp.purchase_requisition.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import id.putra.core_erp.shared.entity.Supplier;

public interface SupplierRepo extends CrudRepository<Supplier, String> {
    List<Supplier> findAllByStatus(String status);

    int countByNameContainingIgnoreCase(String name);

    Page<Supplier> findAllByNameContainingIgnoreCaseAndStatusStartingWithIgnoreCase(String name, String status,
            Pageable pageable);
}
