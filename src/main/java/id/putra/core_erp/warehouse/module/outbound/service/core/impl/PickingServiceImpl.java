package id.putra.core_erp.warehouse.module.outbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.PickingTask;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.PickingTaskCommandAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.PickingTaskQueryAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.core.PickingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PickingServiceImpl implements PickingService {
    @Inject
    private PickingTaskCommandAdapter pickingTaskCommandAdapter;
    @Inject
    private PickingTaskQueryAdapter pickingTaskQueryAdapter;

    @Override
    public PickingTask create(PickingTask dto) {
        pickingTaskCommandAdapter.add(List.of(dto));
        return dto;
    }

    @Override
    public PickingTask update(PickingTask dto) {
        pickingTaskCommandAdapter.update(List.of(dto));
        return dto;
    }

    @Override
    public Boolean delete(Long id) {
        PickingTask dto = new PickingTask();
        dto.setId(id);
        pickingTaskCommandAdapter.delete(List.of(dto));
        return true;
    }

    @Override
    public PickingTask getById(Long id) {
        return pickingTaskQueryAdapter.getById(id);
    }

    @Override
    public List<PickingTask> getAll(PickingTask filter) {
        return pickingTaskQueryAdapter.getAll(filter);
    }
}
