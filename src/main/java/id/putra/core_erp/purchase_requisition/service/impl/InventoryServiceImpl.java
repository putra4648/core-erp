package id.putra.core_erp.purchase_requisition.service.impl;

import java.util.List;

import id.putra.core_erp.purchase_requisition.model.Inventory;
import id.putra.core_erp.purchase_requisition.repository.InventoryRepo;
import id.putra.core_erp.purchase_requisition.service.InventoryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InventoryServiceImpl implements InventoryService {

    @Inject
    private InventoryRepo inventoryRepo;

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
