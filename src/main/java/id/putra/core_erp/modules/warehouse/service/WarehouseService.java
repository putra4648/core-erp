package id.putra.core_erp.modules.warehouse.service;

import java.util.List;

import id.putra.core_erp.modules.warehouse.model.Warehouse;
import id.putra.core_erp.modules.warehouse.repository.WarehouseRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class WarehouseService implements BaseService<Warehouse> {
	private final WarehouseRepository warehouseRepository;

	public WarehouseService(WarehouseRepository warehouseRepository) {
		this.warehouseRepository = warehouseRepository;
	}

	@Override
	public void save(Warehouse entity) {
		warehouseRepository.persist(entity);
	}

	@Override
	public Warehouse findById(Long id) {
		return warehouseRepository.findById(id);
	}

	@Override
	public List<Warehouse> findAll(Warehouse w, Page p) {
		return warehouseRepository.findAll().list();
	}

	@Override
	public void update(Warehouse entity) {
		warehouseRepository.getEntityManager().merge(entity);
	}

	@Override
	public void delete(Long id) {
		warehouseRepository.deleteById(id);
	}

}
