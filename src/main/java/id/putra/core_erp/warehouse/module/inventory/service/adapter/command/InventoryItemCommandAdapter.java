package id.putra.core_erp.warehouse.module.inventory.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.inventory.model.entity.InventoryItem;

public interface InventoryItemCommandAdapter {

    void save(List<InventoryItem> items);

    void update(List<InventoryItem> items);

    void delete(List<InventoryItem> items);

}
