package id.putra.core_erp.purchase_requisition.service;

import java.util.List;

import id.putra.core_erp.purchase_requisition.model.Inventory;

public interface InventoryService {
    void add(Inventory inventory);

    void update(Inventory inventory);

    void delete(Long id);

    List<Inventory> getAll(Inventory inventory);

    Inventory getInventory(Inventory dto);
}
