package id.putra.core_erp.warehouse.module.inbound.service.adapter.command.impl;

import id.putra.core_erp.warehouse.module.inbound.model.entity.QualityInspection;
import id.putra.core_erp.warehouse.module.inbound.model.repository.QualityInspectionRepository;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.InspectionCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InspectionCommandAdapterImpl implements InspectionCommandAdapter {

    @Inject
    private QualityInspectionRepository qualityInspectionRepository;

    @Override
    public QualityInspection add(QualityInspection dto) {
        QualityInspection inspection = new QualityInspection();
        inspection.setId(dto.getId());
        // inspection.setResult(dto.getResult());
        // inspection.setRemarks(dto.getRemarks());
        // inspection.setReceivingLine(dto.getReceivingLine());
        qualityInspectionRepository.persist(inspection);
        return dto;
    }

    @Override
    public QualityInspection update(QualityInspection dto) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }
}
