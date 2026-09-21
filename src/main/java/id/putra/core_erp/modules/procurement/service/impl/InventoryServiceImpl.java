package id.putra.core_erp.modules.procurement.service.impl;

import java.util.List;

import id.putra.core_erp.modules.procurement.model.Inventory;
import id.putra.core_erp.modules.procurement.repository.InventoryRepo;
import id.putra.core_erp.modules.procurement.service.InventoryService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepo inventoryRepo;

    public InventoryServiceImpl(InventoryRepo inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

    @Override
    public void add(Inventory inventory) {
        inventoryRepo.persist(inventory);
    }

    @Override
    public List<Inventory> getAll(Inventory dto) {
        return inventoryRepo.findAll().list();
    }

    @Override
    public Inventory getInventory(Inventory inventory) {
        return inventoryRepo.findByPartNumber(inventory.getPartNumber());
    }

    @Override
    public void update(Inventory inventory) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
