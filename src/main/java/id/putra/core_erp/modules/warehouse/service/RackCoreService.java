package id.putra.core_erp.modules.warehouse.service;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
import id.putra.core_erp.modules.warehouse.model.Rack;
import id.putra.core_erp.modules.warehouse.repository.RackRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class RackCoreService implements BaseService<Rack> {
	private final RackRepository rackRepository;

<<<<<<< HEAD
	public RackCoreService(RackRepository rackRepository) {
		this.rackRepository = rackRepository;
	}

	@Override
	public void delete(Long id) {
		rackRepository.deleteById(id);
	}

	@Override
	public List<Rack> findAll(Rack r, Page p) {
		return rackRepository.findAll().list();
=======
	private final RackRepository rackRepository;

	public RackCoreService(RackRepository rackRepository) {
		this.rackRepository = rackRepository;
	}

	@Override
	public void save(Rack entity) {
		rackRepository.persist(entity);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	@Override
	public Rack findById(Long id) {
		return rackRepository.findById(id);
	}

	@Override
<<<<<<< HEAD
	public void save(Rack entity) {
		rackRepository.persist(entity);
=======
	public Iterable<Rack> findAll(Rack r, Page p) {
		return rackRepository.findAll().list();
	}

	@Override
	public void update(Rack entity) {
		rackRepository.getEntityManager().merge(entity);
	}

	@Override
	public void delete(Long id) {
		rackRepository.deleteById(id);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	@Override
	public void update(Rack entity) {
		rackRepository.update("name = ?1, zoneId = ?2, rows = ?3, cols = ?4 where id = ?5", entity.getName(),
				entity.getZone().getId(), entity.getRows(), entity.getCols(), entity.getId());
	}
}
