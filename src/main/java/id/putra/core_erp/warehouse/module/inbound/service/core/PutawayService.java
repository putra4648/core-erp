package id.putra.core_erp.warehouse.module.inbound.service.core;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.dto.PutawayDto;

public interface PutawayService {
    PutawayDto create(PutawayDto dto);

    PutawayDto update(PutawayDto dto);

    Boolean delete(Long id);

    PutawayDto getById(Long id);

    List<PutawayDto> getAll(PutawayDto filter);
}

