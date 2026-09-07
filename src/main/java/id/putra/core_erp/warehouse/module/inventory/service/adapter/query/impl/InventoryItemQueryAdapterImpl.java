package id.putra.core_erp.warehouse.module.inventory.service.adapter.query.impl;

import id.putra.core_erp.warehouse.module.inventory.dto.InventoryItemDto;
import id.putra.core_erp.warehouse.module.inventory.model.repository.InventoryItemRepository;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.query.InventoryItemQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InventoryItemQueryAdapterImpl implements InventoryItemQueryAdapter {

    @Inject
    private InventoryItemRepository inventoryItemRepository;

    @Override
    public InventoryItemDto getInventoryItemById(InventoryItemDto dto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
