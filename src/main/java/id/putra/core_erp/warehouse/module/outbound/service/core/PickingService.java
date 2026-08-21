package id.putra.core_erp.warehouse.module.outbound.service.core;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.dto.PickingTaskDto;

public interface PickingService {
    PickingTaskDto create(PickingTaskDto dto);
    PickingTaskDto update(PickingTaskDto dto);
    Boolean delete(Long id);
    PickingTaskDto getById(Long id);
    List<PickingTaskDto> getAll(PickingTaskDto filter);
}

