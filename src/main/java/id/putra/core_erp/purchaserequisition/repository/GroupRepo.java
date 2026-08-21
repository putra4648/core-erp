package id.putra.core_erp.purchaserequisition.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import id.putra.core_erp.purchaserequisition.model.Group;

public interface GroupRepo extends CrudRepository<Group, String> {
    Optional<Group> findByNameContainingIgnoreCase(String name);
}

