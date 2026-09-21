package id.putra.core_erp.modules.outbound.service.impl;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.PickingTask;
import id.putra.core_erp.modules.outbound.repository.PickingTaskRepository;
import id.putra.core_erp.modules.outbound.service.PickingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PickingServiceImpl implements PickingService {

    private final PickingTaskRepository pickingTaskRepository;

    public PickingServiceImpl(PickingTaskRepository pickingTaskRepository) {
        this.pickingTaskRepository = pickingTaskRepository;
    }

    @Override
    public PickingTask create(PickingTask dto) {
        pickingTaskRepository.persist(dto);
        return dto;
    }

    @Override
    public PickingTask update(PickingTask dto) {
        return pickingTaskRepository.getEntityManager().merge(dto);
    }

    @Override
    public Boolean delete(Long id) {
        return pickingTaskRepository.deleteById(id);
    }

    @Override
    public PickingTask getById(Long id) {
        return pickingTaskRepository.findById(id);
    }

    @Override
    public List<PickingTask> getAll(PickingTask filter) {
        return pickingTaskRepository.findAll().list();
    }
}
