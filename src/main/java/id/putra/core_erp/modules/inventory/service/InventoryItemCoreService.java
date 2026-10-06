package id.putra.core_erp.modules.inventory.service;

import java.util.List;

import id.putra.core_erp.modules.inventory.model.InventoryItem;
import id.putra.core_erp.modules.inventory.repository.InventoryItemRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class InventoryItemCoreService implements BaseService<InventoryItem> {
	private final InventoryItemRepository inventoryItemRepository;

	public InventoryItemCoreService(InventoryItemRepository inventoryItemRepository) {
		this.inventoryItemRepository = inventoryItemRepository;
	}

	@Override
	public InventoryItem findById(Long id) {
		return inventoryItemRepository.findById(id);
	}

	@Override
	public void save(InventoryItem entity) {
		inventoryItemRepository.persist(entity);
	}

	@Override
	public List<InventoryItem> findAll(InventoryItem item, Page p) {
		return inventoryItemRepository.findAll().list();
	}

	@Override
	public void update(InventoryItem entity) {
		inventoryItemRepository.getEntityManager().merge(entity);
	}

	@Override
	public void delete(Long id) {
		inventoryItemRepository.deleteById(id);
	}

}
