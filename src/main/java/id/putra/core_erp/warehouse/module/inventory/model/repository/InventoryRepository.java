package id.putra.core_erp.warehouse.module.inventory.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import id.putra.core_erp.warehouse.module.inventory.model.entity.InventoryItem;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
}

