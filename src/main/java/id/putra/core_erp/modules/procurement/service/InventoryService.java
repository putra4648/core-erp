package id.putra.core_erp.modules.procurement.service;

import java.util.List;

import id.putra.core_erp.modules.procurement.model.Inventory;
import id.putra.core_erp.modules.procurement.repository.InventoryRepo;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryService implements BaseService<Inventory> {
    private final InventoryRepo inventoryRepo;

    public InventoryService(InventoryRepo inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

    public void save(Inventory entity) {
        inventoryRepo.persist(entity);
    }

    public Inventory findById(Long id) {
        return inventoryRepo.findById(id);
    }

    public List<Inventory> findAll(Inventory i, Page p) {
        return inventoryRepo.findAll().list();
    }

    public void delete(Long id) {
        inventoryRepo.deleteById(id);
    }

    @Override
    public void update(Inventory entity) {
        inventoryRepo.update(
                "poNumber = ?1, partNumber = ?2, partName = ?3, description = ?4, area = ?5, shelf = ?6, supplier = ?7, unit = ?8, qty = ?9, itemArea = ?10, date = ?11, location = ?12, uom = ?13 where id = ?14",
                entity.getPoNumber(), entity.getPartNumber(), entity.getPartName(), entity.getDescription(),
                entity.getArea(), entity.getShelf(), entity.getSupplier(), entity.getUnit(), entity.getQty(),
                entity.getItemArea(), entity.getDate(), entity.getLocation(), entity.getUom(), entity.getId());
    }
}
