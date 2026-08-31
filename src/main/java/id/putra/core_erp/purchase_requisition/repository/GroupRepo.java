package id.putra.core_erp.purchase_requisition.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import id.putra.core_erp.purchase_requisition.model.Group;

public interface GroupRepo extends CrudRepository<Group, String> {
    Optional<Group> findByNameContainingIgnoreCase(String name);
}
