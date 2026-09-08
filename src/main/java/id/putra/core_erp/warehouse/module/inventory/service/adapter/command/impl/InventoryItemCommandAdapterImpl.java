package id.putra.core_erp.warehouse.module.inventory.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inventory.model.entity.InventoryItem;
import id.putra.core_erp.warehouse.module.inventory.model.repository.InventoryItemRepository;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.command.InventoryItemCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InventoryItemCommandAdapterImpl implements InventoryItemCommandAdapter {

    @Inject
    private InventoryItemRepository inventoryItemRepository;

    @Override
    public void save(List<InventoryItem> items) {
        for (InventoryItem item : items) {
            inventoryItemRepository.persist(item);
        }
    }

    @Override
    public void update(List<InventoryItem> items) {
        for (InventoryItem item : items) {
            inventoryItemRepository.update("name = ?1", item.getName());
        }
    }

    @Override
    public void delete(List<InventoryItem> items) {
        for (InventoryItem item : items) {
            inventoryItemRepository.delete(item);
        }
    }

}
