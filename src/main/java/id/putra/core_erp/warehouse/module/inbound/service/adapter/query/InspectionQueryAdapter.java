package id.putra.core_erp.warehouse.module.inbound.service.adapter.query;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.QualityInspection;

public interface InspectionQueryAdapter {
    QualityInspection getById(Long id);

    List<QualityInspection> getAll(QualityInspection filter);
}
