package id.putra.core_erp.warehouse.module.outbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.PickingTask;
import id.putra.core_erp.warehouse.module.outbound.model.repository.PickingTaskRepository;
import id.putra.core_erp.warehouse.module.outbound.service.core.PickingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PickingServiceImpl implements PickingService {
    @Inject
    private PickingTaskRepository pickingTaskRepository;

    @Override
    public PickingTask create(PickingTask dto) {
        pickingTaskRepository.persist(dto);
        return dto;
    }

    @Override
    public PickingTask update(PickingTask dto) {
        pickingTaskRepository.getEntityManager().merge(dto);
        return dto;
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
