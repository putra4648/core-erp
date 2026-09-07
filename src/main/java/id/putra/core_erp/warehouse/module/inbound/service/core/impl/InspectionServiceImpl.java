package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.dto.QualityInspectionDto;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.InspectionCommandAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.InspectionQueryAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.core.InspectionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InspectionServiceImpl implements InspectionService {

    @Inject
    private InspectionCommandAdapter inspectionCommandAdapter;

    @Inject
    private InspectionQueryAdapter inspectionQueryAdapter;

    @Override
    public QualityInspectionDto create(QualityInspectionDto dto) {
        return inspectionCommandAdapter.add(dto);
    }

    @Override
    public QualityInspectionDto update(QualityInspectionDto dto) {
        return inspectionCommandAdapter.update(dto);
    }

    @Override
    public Boolean delete(Long id) {
        return inspectionCommandAdapter.delete(id);
    }

    @Override
    public QualityInspectionDto getById(Long id) {
        return inspectionQueryAdapter.getById(id);
    }

    @Override
    public List<QualityInspectionDto> getAll(QualityInspectionDto filter) {
        return inspectionQueryAdapter.getAll(filter);
    }
}
