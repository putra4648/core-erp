package id.putra.core_erp.modules.procurement.service;

import java.util.List;

import id.putra.core_erp.modules.procurement.model.Item;
import id.putra.core_erp.modules.procurement.repository.ItemRepo;
import id.putra.core_erp.modules.procurement.repository.SupplierRepo;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ItemService implements BaseService<Item> {

	private final ItemRepo itemRepo;

	public ItemService(ItemRepo itemRepo, SupplierRepo supplierRepo) {
		this.itemRepo = itemRepo;
	}

	@Override
	public void save(Item entity) {
		itemRepo.persist(entity);

	}

	@Override
	public Item findById(Long id) {
		return itemRepo.findById(id);
	}

	@Override
	public void delete(Long id) {
		itemRepo.deleteById(id);

	}

	@Override
	public void update(Item entity) {
		itemRepo.getEntityManager().merge(entity);

	}

	@Override
	public List<Item> findAll(Item entity, Page page) {
		return itemRepo.findAll().page(page).list();
	}

}
