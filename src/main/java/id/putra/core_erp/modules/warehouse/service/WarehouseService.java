package id.putra.core_erp.modules.warehouse.service;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
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

<<<<<<< HEAD
	public WarehouseService(WarehouseRepository warehouseRepository) {
		this.warehouseRepository = warehouseRepository;
	}

	@Override
	public void delete(Long id) {
		warehouseRepository.deleteById(id);
	}

	@Override
	public List<Warehouse> findAll(Warehouse w, Page p) {
		return warehouseRepository.findAll().list();
=======
	private final WarehouseRepository warehouseRepository;

	public WarehouseService(WarehouseRepository warehouseRepository) {
		this.warehouseRepository = warehouseRepository;
	}

	@Override
	public void save(Warehouse entity) {
		warehouseRepository.persist(entity);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	@Override
	public Warehouse findById(Long id) {
		return warehouseRepository.findById(id);
	}

	@Override
<<<<<<< HEAD
	public void save(Warehouse entity) {
		warehouseRepository.persist(entity);
=======
	public Iterable<Warehouse> findAll(Warehouse w, Page p) {
		return warehouseRepository.findAll().list();
	}

	@Override
	public void update(Warehouse entity) {
		warehouseRepository.getEntityManager().merge(entity);
	}

	@Override
	public void delete(Long id) {
		warehouseRepository.deleteById(id);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	@Override
	public void update(Warehouse entity) {
		warehouseRepository.update("name = ?1 where id = ?2", entity.getName(), entity.getId());
	}
}
