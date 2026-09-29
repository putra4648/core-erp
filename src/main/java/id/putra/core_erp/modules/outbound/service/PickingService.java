package id.putra.core_erp.modules.outbound.service;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.PickingTask;
import id.putra.core_erp.modules.outbound.repository.PickingTaskRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PickingService implements BaseService<PickingTask> {
	private final PickingTaskRepository pickingTaskRepository;

	public PickingService(PickingTaskRepository pickingTaskRepository) {
		this.pickingTaskRepository = pickingTaskRepository;
	}

	@Override
	public void delete(Long id) {
		pickingTaskRepository.deleteById(id);
	}

	@Override
	public List<PickingTask> findAll(PickingTask s, Page p) {
		return pickingTaskRepository.findAll().list();
	}

	@Override
	public PickingTask findById(Long id) {
		return pickingTaskRepository.findById(id);
	}

	@Override
	public void save(PickingTask entity) {
		pickingTaskRepository.persist(entity);
	}

	@Override
	public void update(PickingTask dto) {
		pickingTaskRepository.getEntityManager().merge(dto);
	}
}
