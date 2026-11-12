package id.putra.core_erp.modules.warehouse.service;

import java.util.List;

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

	public RackCoreService(RackRepository rackRepository) {
		this.rackRepository = rackRepository;
	}

	@Override
	public void save(Rack entity) {
		rackRepository.persist(entity);
	}

	@Override
	public Rack findById(Long id) {
		return rackRepository.findById(id);
	}

	@Override
	public List<Rack> findAll(Rack r, Page p) {
		return rackRepository.findAll().list();
	}

	@Override
	public void update(Rack entity) {
		rackRepository.getEntityManager().merge(entity);
	}

	@Override
	public void delete(Long id) {
		rackRepository.softDeleteById(id);
	}

	@Override
	public long count() {
		return rackRepository.count();
	}
}
