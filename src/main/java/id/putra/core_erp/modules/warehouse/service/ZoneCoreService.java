package id.putra.core_erp.modules.warehouse.service;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
import id.putra.core_erp.modules.warehouse.model.Zone;
import id.putra.core_erp.modules.warehouse.repository.ZoneRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ZoneCoreService implements BaseService<Zone> {
	private final ZoneRepository zoneRepository;

<<<<<<< HEAD
	public ZoneCoreService(ZoneRepository zoneRepository) {
		this.zoneRepository = zoneRepository;
	}

	@Override
	public void delete(Long id) {
		zoneRepository.deleteById(id);
	}

	@Override
	public List<Zone> findAll(Zone z, Page p) {
		return zoneRepository.findAll().list();
=======
	private final ZoneRepository zoneRepository;

	public ZoneCoreService(ZoneRepository zoneRepository) {
		this.zoneRepository = zoneRepository;
	}

	@Override
	public void save(Zone entity) {
		zoneRepository.persist(entity);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	@Override
	public Zone findById(Long id) {
		return zoneRepository.findById(id);
	}

	@Override
<<<<<<< HEAD
	public void save(Zone entity) {
		zoneRepository.persist(entity);
=======
	public Iterable<Zone> findAll(Zone z, Page p) {
		return zoneRepository.findAll().list();
	}

	@Override
	public void update(Zone entity) {
		zoneRepository.getEntityManager().merge(entity);
	}

	@Override
	public void delete(Long id) {
		zoneRepository.deleteById(id);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	@Override
	public void update(Zone entity) {
		zoneRepository.update("code = ?1, name = ?2, type = ?3 where id = ?4", entity.getCode(), entity.getName(),
				entity.getType(), entity.getId());
	}
}
