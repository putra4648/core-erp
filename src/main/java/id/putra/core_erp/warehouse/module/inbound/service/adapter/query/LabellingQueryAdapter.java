package id.putra.core_erp.warehouse.module.inbound.service.adapter.query;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.dto.LabellingDto;

public interface LabellingQueryAdapter {
    LabellingDto getById(Long id);

    List<LabellingDto> getAll(LabellingDto filter);
}

