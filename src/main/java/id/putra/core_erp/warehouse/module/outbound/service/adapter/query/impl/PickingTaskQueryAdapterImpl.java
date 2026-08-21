package id.putra.core_erp.warehouse.module.outbound.service.adapter.query.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.putra.core_erp.warehouse.module.outbound.converter.PickingTaskConverter;
import id.putra.core_erp.warehouse.module.outbound.dto.PickingTaskDto;
import id.putra.core_erp.warehouse.module.outbound.model.repository.PickingTaskRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.PickingTaskQueryAdapter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PickingTaskQueryAdapterImpl implements PickingTaskQueryAdapter {
    private final PickingTaskRepository pickingTaskRepository;

    @Override
    public PickingTaskDto getById(Long id) {
        return pickingTaskRepository.findById(id).map(PickingTaskConverter::toDto).orElse(null);
    }

    @Override
    public List<PickingTaskDto> getAll(PickingTaskDto filter) {
        return pickingTaskRepository.findAll().stream().map(PickingTaskConverter::toDto).toList();
    }
}

