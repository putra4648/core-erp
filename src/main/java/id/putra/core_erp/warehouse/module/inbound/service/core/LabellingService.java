package id.putra.core_erp.warehouse.module.inbound.service.core;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.dto.LabellingDto;

public interface LabellingService {
    LabellingDto create(LabellingDto dto);

    LabellingDto update(LabellingDto dto);

    Boolean delete(Long id);

    LabellingDto getById(Long id);

    List<LabellingDto> getAll(LabellingDto filter);
}

