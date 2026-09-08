package id.putra.core_erp.warehouse.module.outbound.service.adapter.query;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.PickingTask;

public interface PickingTaskQueryAdapter {
    PickingTask getById(Long id);

    List<PickingTask> getAll(PickingTask filter);
}
