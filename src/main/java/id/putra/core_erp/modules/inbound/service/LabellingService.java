package id.putra.core_erp.modules.inbound.service;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.Labelling;
import id.putra.core_erp.modules.inbound.repository.LabellingRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class LabellingService implements BaseService<Labelling> {

	private final LabellingRepository labellingRepository;

	public LabellingService(LabellingRepository labellingRepository) {
		this.labellingRepository = labellingRepository;
	}

	@Override
	public void save(Labelling entity) {
		labellingRepository.persist(entity);
	}

	public Labelling create(Labelling dto) {
		labellingRepository.persist(dto);
		return dto;
	}

	@Override
	public void update(Labelling dto) {
		labellingRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		labellingRepository.deleteById(id);
	}

	@Override
	public Labelling findById(Long id) {
		return labellingRepository.findById(id);
	}

	public Labelling getById(Long id) {
		return findById(id);
	}

	@Override
	public Iterable<Labelling> findAll(Labelling s, Page p) {
		return labellingRepository.findAll().list();
	}

	public List<Labelling> getAll(Labelling filter) {
		return labellingRepository.findAll().list();
	}
}
