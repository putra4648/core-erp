package id.putra.core_erp.modules.procurement.service;

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
	private final SupplierRepo supplierRepo;

	public ItemService(ItemRepo itemRepo, SupplierRepo supplierRepo) {
		this.itemRepo = itemRepo;
		this.supplierRepo = supplierRepo;
	}

	@Override
	public void save(Item entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Item findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Item entity) {
		itemRepo.getEntityManager().merge(entity);

	}

	@Override
	public Iterable<Item> findAll(Item entity, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

}
