package id.putra.core_erp.modules.outbound.service;

import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.PickingTask;
import id.putra.core_erp.modules.outbound.repository.PickingTaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PickingService implements BaseService<PickingTask> {

    private final PickingTaskRepository pickingTaskRepository;

    public PickingService(PickingTaskRepository pickingTaskRepository) {
        this.pickingTaskRepository = pickingTaskRepository;
    }

    public void save(PickingTask entity) {
        pickingTaskRepository.persist(entity);
    }

    public PickingTask create(PickingTask dto) {
        pickingTaskRepository.persist(dto);
        return dto;
    }

    public void update(PickingTask dto) {
        pickingTaskRepository.getEntityManager().merge(dto);
    }

    public void delete(Long id) {
        pickingTaskRepository.deleteById(id);
    }

    public PickingTask getById(Long id) {
        return pickingTaskRepository.findById(id);
    }

    public PickingTask findById(Long id) {
        return pickingTaskRepository.findById(id);
    }

    public Iterable<PickingTask> findAll(PickingTask s, Page p) {
        return pickingTaskRepository.findAll().list();
    }

    public List<PickingTask> getAll(PickingTask filter) {
        return pickingTaskRepository.findAll().list();
    }
}
