package id.putra.core_erp.modules.outbound.service;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.PickingTask;

public interface PickingService {
    PickingTask create(PickingTask dto);

    PickingTask update(PickingTask dto);

    Boolean delete(Long id);

    PickingTask getById(Long id);

    List<PickingTask> getAll(PickingTask filter);
}
