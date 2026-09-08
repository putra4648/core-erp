package id.putra.core_erp.warehouse.module.outbound.service.adapter.query.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.dto.PickingTaskDto;
import id.putra.core_erp.warehouse.module.outbound.model.repository.PickingTaskRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.PickingTaskQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PickingTaskQueryAdapterImpl implements PickingTaskQueryAdapter {
    @Inject
    private PickingTaskRepository pickingTaskRepository;

    @Override
    public PickingTaskDto getById(Long id) {
        return pickingTaskRepository.findById(id).map(e -> new PickingTaskDto()).orElse(null);
    }

    @Override
    public List<PickingTaskDto> getAll(PickingTaskDto filter) {
        return pickingTaskRepository.findAll().list().stream().map(e -> new PickingTaskDto()).toList();
    }
}
