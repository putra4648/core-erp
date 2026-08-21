package id.putra.core_erp.warehouse.module.inbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.inbound.dto.LabellingDto;

public interface LabellingCommandAdapter {
    LabellingDto add(LabellingDto dto);

    LabellingDto update(LabellingDto dto);

    Boolean delete(LabellingDto dto);
}

