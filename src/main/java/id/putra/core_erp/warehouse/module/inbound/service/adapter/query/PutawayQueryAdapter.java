package id.putra.core_erp.warehouse.module.inbound.service.adapter.query;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.dto.PutawayDto;

public interface PutawayQueryAdapter {
    PutawayDto getById(Long id);

    List<PutawayDto> getAll(PutawayDto filter);
}

