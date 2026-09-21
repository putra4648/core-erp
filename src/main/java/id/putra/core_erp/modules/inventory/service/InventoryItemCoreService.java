package id.putra.core_erp.modules.inventory.service;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.inventory.model.InventoryItem;
import io.quarkus.panache.common.Page;

public interface InventoryItemCoreService {

    InventoryItem getInventoryItem(InventoryItem item);

    void save(List<InventoryItem> items);

    void update(List<InventoryItem> items);

    void delete(List<InventoryItem> items);

    DataProvider<InventoryItem, Void> getInventoryItems(String value, Page ofSize);

}
