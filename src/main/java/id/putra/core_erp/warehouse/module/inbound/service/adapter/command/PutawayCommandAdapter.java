package id.putra.core_erp.warehouse.module.inbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.inbound.dto.PutawayDto;

public interface PutawayCommandAdapter {
    PutawayDto add(PutawayDto dto);

    PutawayDto update(PutawayDto dto);

    Boolean delete(PutawayDto dto);
}

