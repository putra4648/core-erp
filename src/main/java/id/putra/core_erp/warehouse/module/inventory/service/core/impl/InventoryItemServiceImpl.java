package id.putra.core_erp.warehouse.module.inventory.service.core.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.inventory.model.entity.InventoryItem;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.command.InventoryItemCommandAdapter;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.query.InventoryItemQueryAdapter;
import id.putra.core_erp.warehouse.module.inventory.service.core.InventoryItemCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InventoryItemServiceImpl implements InventoryItemCoreService {
    @Inject
    private InventoryItemQueryAdapter inventoryItemQueryAdapter;

    @Inject
    private InventoryItemCommandAdapter inventoryItemCommandAdapter;

    @Override
    public InventoryItem getInventoryItem(InventoryItem item) {
        return inventoryItemQueryAdapter.getInventoryItemById(item);
    }

    @Override
    public void save(List<InventoryItem> items) {
        inventoryItemCommandAdapter.save(items);
    }

    @Override
    public void update(List<InventoryItem> dtos) {
        inventoryItemCommandAdapter.update(dtos);
    }

    @Override
    public void delete(List<InventoryItem> dtos) {
        inventoryItemCommandAdapter.delete(dtos);
    }

    @Override
    public DataProvider<InventoryItem, Void> getInventoryItems(String value, Page ofSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInventoryItems'");
    }

}
