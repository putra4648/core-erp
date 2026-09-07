package id.putra.core_erp.warehouse.module.outbound.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.dto.PickingTaskDto;
import id.putra.core_erp.warehouse.module.outbound.model.repository.PickingTaskRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.PickingTaskCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PickingTaskCommandAdapterImpl implements PickingTaskCommandAdapter {
    @Inject
    private PickingTaskRepository pickingTaskRepository;

    @Override
    public void add(List<PickingTaskDto> dtos) {
    }

    @Override
    public void update(List<PickingTaskDto> dtos) {
    }

    @Override
    public void delete(List<PickingTaskDto> dtos) {

    }
}
