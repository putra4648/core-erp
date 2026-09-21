package id.putra.core_erp.modules.procurement.service;

import id.putra.core_erp.shared.service.BaseService;

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
    public Inventory save(Inventory entity) {
        inventoryRepo.persist(entity);
        return entity;
    }
    public Inventory findById(Long id) {
        return inventoryRepo.findById(id);
    }
    public Iterable<Inventory> findAll() {
        return inventoryRepo.findAll().list();
    }
    public Inventory getInventory(Inventory inventory) {
        return inventoryRepo.findByPartNumber(inventory.getPartNumber());
    }
    public void update(Inventory inventory) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
