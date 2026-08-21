package id.putra.core_erp.warehouse.module.inbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.inbound.dto.ReturnDto;

public interface ReturnInboundCommandAdapter {
    ReturnDto add(ReturnDto dto);

    ReturnDto update(ReturnDto dto);

    Boolean delete(ReturnDto dto);
}

