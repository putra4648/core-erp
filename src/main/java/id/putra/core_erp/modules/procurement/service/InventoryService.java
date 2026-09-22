package id.putra.core_erp.modules.procurement.service;

import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;

import java.util.List;

import id.putra.core_erp.modules.procurement.model.Inventory;
import id.putra.core_erp.modules.procurement.repository.InventoryRepo;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryService implements BaseService<Inventory> {

    private final InventoryRepo inventoryRepo;

    public InventoryService(InventoryRepo inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

    public void add(Inventory inventory) {
        inventoryRepo.persist(inventory);
    }

    public List<Inventory> getAll(Inventory dto) {
        return inventoryRepo.findAll().list();
    }

    public void save(Inventory entity) {
        inventoryRepo.persist(entity);
    }

    public Inventory findById(Long id) {
        return inventoryRepo.findById(id);
    }

    public Iterable<Inventory> findAll(Inventory i, Page p) {
        return inventoryRepo.findAll().list();
    }

    public Inventory getInventory(Inventory inventory) {
        return inventoryRepo.findByPartNumber(inventory.getPartNumber());
    }

    public void update(Inventory inventory) {
        inventoryRepo.update("partName = ?1", inventory.getPartName());
    }

    public void delete(Long id) {
        inventoryRepo.deleteById(id);
    }

}
