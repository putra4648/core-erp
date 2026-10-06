package id.putra.core_erp.modules.procurement.service;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
import id.putra.core_erp.modules.procurement.model.Inventory;
import id.putra.core_erp.modules.procurement.repository.InventoryRepo;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryService implements BaseService<Inventory> {
	private final InventoryRepo inventoryRepo;

<<<<<<< HEAD
	public InventoryService(InventoryRepo inventoryRepo) {
		this.inventoryRepo = inventoryRepo;
	}

	@Override
	public void delete(Long id) {
		inventoryRepo.deleteById(id);
	}

	@Override
	public List<Inventory> findAll(Inventory i, Page p) {
		return inventoryRepo.findAll().list();
=======
	private final InventoryRepo inventoryRepo;

	public InventoryService(InventoryRepo inventoryRepo) {
		this.inventoryRepo = inventoryRepo;
	}

	@Override
	public void save(Inventory entity) {
		inventoryRepo.persist(entity);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	@Override
	public Inventory findById(Long id) {
		return inventoryRepo.findById(id);
	}

	@Override
<<<<<<< HEAD
	public void save(Inventory entity) {
		inventoryRepo.persist(entity);
=======
	public Iterable<Inventory> findAll(Inventory i, Page p) {
		return inventoryRepo.findAll().list();
	}

	@Override
	public void update(Inventory entity) {
		inventoryRepo.getEntityManager().merge(entity);
	}

	@Override
	public void delete(Long id) {
		inventoryRepo.deleteById(id);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
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
