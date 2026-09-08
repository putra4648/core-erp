package id.putra.core_erp.warehouse.module.inbound.service.core;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.QualityInspection;

public interface InspectionService {
    QualityInspection create(QualityInspection dto);

    QualityInspection update(QualityInspection dto);

    Boolean delete(Long id);

    QualityInspection getById(Long id);

    List<QualityInspection> getAll(QualityInspection filter);
}
