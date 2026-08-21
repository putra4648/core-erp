package id.putra.core_erp.warehouse.module.inbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.inbound.dto.QualityInspectionDto;

public interface InspectionCommandAdapter {
    QualityInspectionDto add(QualityInspectionDto dto);

    QualityInspectionDto update(QualityInspectionDto dto);

    Boolean delete(Long id);
}

