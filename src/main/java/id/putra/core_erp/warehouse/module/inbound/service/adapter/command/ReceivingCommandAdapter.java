package id.putra.core_erp.warehouse.module.inbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.inbound.dto.ReceivingDto;

public interface ReceivingCommandAdapter {
    ReceivingDto add(ReceivingDto dto);

    ReceivingDto update(ReceivingDto dto);

    Boolean delete(Long id);

}
