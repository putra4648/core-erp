package id.putra.core_erp.warehouse.module.inbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.inbound.model.entity.QualityInspection;

public interface InspectionCommandAdapter {
    QualityInspection add(QualityInspection dto);

    QualityInspection update(QualityInspection dto);

    Boolean delete(Long id);
}
