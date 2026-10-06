package id.putra.core_erp.modules.outbound.service;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
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

<<<<<<< HEAD
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
=======
	private final PickingTaskRepository pickingTaskRepository;

	public PickingService(PickingTaskRepository pickingTaskRepository) {
		this.pickingTaskRepository = pickingTaskRepository;
	}

	@Override
	public void save(PickingTask entity) {
		pickingTaskRepository.persist(entity);
	}

	@Override
	public void update(PickingTask dto) {
		pickingTaskRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		pickingTaskRepository.deleteById(id);
	}

	@Override
	public PickingTask findById(Long id) {
		return pickingTaskRepository.findById(id);
	}

	@Override
	public Iterable<PickingTask> findAll(PickingTask s, Page p) {
		return pickingTaskRepository.findAll().list();
	}

>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
}
