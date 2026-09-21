package id.putra.core_erp.modules.inbound.service;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.QualityInspection;

public interface InspectionService {
    QualityInspection create(QualityInspection dto);

    QualityInspection update(QualityInspection dto);

    Boolean delete(Long id);

    QualityInspection getById(Long id);

    List<QualityInspection> getAll(QualityInspection filter);
}
