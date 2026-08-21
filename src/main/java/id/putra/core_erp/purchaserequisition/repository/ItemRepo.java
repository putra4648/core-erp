package id.putra.core_erp.purchaserequisition.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import id.putra.core_erp.purchaserequisition.model.Item;
import id.putra.core_erp.shared.entity.Supplier;

public interface ItemRepo extends CrudRepository<Item, String> {

    Page<Item> findByNameContainingIgnoreCaseAndStatusContainingIgnoreCaseOrSupplierEquals(
            String name,
            String status,
            Supplier supplier,
            Pageable pageable);

    long countByNameContainingIgnoreCase(String name);

    Optional<Item> findByNameEqualsIgnoreCase(String name);

}


