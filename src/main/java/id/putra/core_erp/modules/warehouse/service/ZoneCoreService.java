package id.putra.core_erp.modules.warehouse.service;

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

	public ZoneCoreService(ZoneRepository zoneRepository) {
		this.zoneRepository = zoneRepository;
	}

	@Override
	public void save(Zone entity) {
		zoneRepository.persist(entity);
	}

	@Override
	public Zone findById(Long id) {
		return zoneRepository.findById(id);
	}

	@Override
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
	}

}
