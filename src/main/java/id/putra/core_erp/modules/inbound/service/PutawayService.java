package id.putra.core_erp.modules.inbound.service;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.Putaway;
import id.putra.core_erp.modules.inbound.repository.PutawayRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PutawayService implements BaseService<Putaway> {
	private final PutawayRepository putawayRepository;

	public PutawayService(PutawayRepository putawayRepository) {
		this.putawayRepository = putawayRepository;
	}

	@Override
	public void save(Putaway entity) {
		putawayRepository.persist(entity);
	}

	@Override
	public void update(Putaway dto) {
		putawayRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		putawayRepository.deleteById(id);
	}

	@Override
	public Putaway findById(Long id) {
		return putawayRepository.findById(id);
	}

	@Override
	public List<Putaway> findAll(Putaway putAway, Page p) {
		return putawayRepository.findAll().list();
	}

}
