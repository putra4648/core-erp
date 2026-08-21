package id.putra.core_erp.warehouse.module.inbound.service.core;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.dto.ReturnDto;

public interface ReturnInboundService {
    ReturnDto create(ReturnDto dto);

    ReturnDto update(ReturnDto dto);

    Boolean delete(Long id);

    ReturnDto getById(Long id);

    List<ReturnDto> getAll(ReturnDto filter);
}

