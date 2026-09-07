package id.putra.core_erp.warehouse.module.inventory.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inventory.dto.InventoryItemDto;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.command.InventoryItemCommandAdapter;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.query.InventoryItemQueryAdapter;
import id.putra.core_erp.warehouse.module.inventory.service.core.InventoryItemCoreService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InventoryItemServiceImpl implements InventoryItemCoreService {
    @Inject
    private InventoryItemQueryAdapter inventoryItemQueryAdapter;

    @Inject
    private InventoryItemCommandAdapter inventoryItemCommandAdapter;

    @Override
    public InventoryItemDto getInventoryItem(InventoryItemDto dto) {
        return inventoryItemQueryAdapter.getInventoryItemById(dto);
    }

    @Override
    public void save(List<InventoryItemDto> dtos) {
        inventoryItemCommandAdapterpersist(dtos);
    }

    @Override
    public void update(List<InventoryItemDto> dtos) {
        inventoryItemCommandAdapter.update(dtos);
    }

    @Override
    public void delete(List<InventoryItemDto> dtos) {
        inventoryItemCommandAdapter.delete(dtos);
    }

}
