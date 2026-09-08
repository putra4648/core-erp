package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.QualityInspection;
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
    public QualityInspection create(QualityInspection dto) {
        return inspectionCommandAdapter.add(dto);
    }

    @Override
    public QualityInspection update(QualityInspection dto) {
        return inspectionCommandAdapter.update(dto);
    }

    @Override
    public Boolean delete(Long id) {
        return inspectionCommandAdapter.delete(id);
    }

    @Override
    public QualityInspection getById(Long id) {
        return inspectionQueryAdapter.getById(id);
    }

    @Override
    public List<QualityInspection> getAll(QualityInspection filter) {
        return inspectionQueryAdapter.getAll(filter);
    }
}
