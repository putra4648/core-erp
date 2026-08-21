package id.putra.core_erp.warehouse.module.outbound.service.adapter.command.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.putra.core_erp.warehouse.module.outbound.converter.PickingTaskConverter;
import id.putra.core_erp.warehouse.module.outbound.dto.PickingTaskDto;
import id.putra.core_erp.warehouse.module.outbound.model.repository.PickingTaskRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.PickingTaskCommandAdapter;
import lombok.RequiredArgsConstructor;

@Service("pickingTaskCommandAdapter")
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class PickingTaskCommandAdapterImpl implements PickingTaskCommandAdapter {

    private final PickingTaskRepository pickingTaskRepository;

    @Override
    public void add(List<PickingTaskDto> dtos) {
        var entities = dtos.stream().map(PickingTaskConverter::toEntity).toList();
        pickingTaskRepository.saveAll(java.util.Objects.requireNonNull(entities));
    }

    @Override
    public void update(List<PickingTaskDto> dtos) {
        var entities = dtos.stream().map(PickingTaskConverter::toEntity).toList();
        pickingTaskRepository.saveAll(java.util.Objects.requireNonNull(entities));
    }

    @Override
    public void delete(List<PickingTaskDto> dtos) {
        var ids = dtos.stream().map(d -> d.getId()).toList();
        pickingTaskRepository.deleteAllById(java.util.Objects.requireNonNull(ids));
    }
}

