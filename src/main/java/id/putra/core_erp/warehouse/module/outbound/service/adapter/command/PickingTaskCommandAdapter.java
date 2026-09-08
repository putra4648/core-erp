package id.putra.core_erp.warehouse.module.outbound.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.PickingTask;

public interface PickingTaskCommandAdapter {
    void add(List<PickingTask> dtos);

    void update(List<PickingTask> dtos);

    void delete(List<PickingTask> dtos);
}
