package id.putra.core_erp.warehouse.module.inventory.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inventory.dto.InventoryItemDto;
import id.putra.core_erp.warehouse.module.inventory.model.repository.InventoryItemRepository;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.command.InventoryItemCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InventoryItemCommandAdapterImpl implements InventoryItemCommandAdapter {

    @Inject
    private InventoryItemRepository inventoryItemRepository;

    @Override
    public void save(List<InventoryItemDto> dtos) {
    }

    @Override
    public void update(List<InventoryItemDto> dtos) {

    }

    @Override
    public void delete(List<InventoryItemDto> dtos) {

    }

}
