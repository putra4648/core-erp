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

	@Override
	public void update(Labelling dto) {
		labellingRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		labellingRepository.softDeleteById(id);
	}

	@Override
	public Labelling findById(Long id) {
		return labellingRepository.findById(id);
	}

	@Override
	public List<Labelling> findAll(Labelling s, Page p) {
		return labellingRepository.findAll().list();
	}

	@Override
	public long count() {
		return labellingRepository.count();
	}
}
