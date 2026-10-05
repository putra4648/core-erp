package id.putra.core_erp.modules.procurement.service;

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

	@Override
	public void save(Inventory entity) {
		inventoryRepo.persist(entity);
	}

	@Override
	public Inventory findById(Long id) {
		return inventoryRepo.findById(id);
	}

	@Override
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
	}

}
