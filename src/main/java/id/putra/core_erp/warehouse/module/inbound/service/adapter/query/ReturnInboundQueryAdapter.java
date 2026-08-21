package id.putra.core_erp.warehouse.module.inbound.service.adapter.query;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.dto.ReturnDto;

public interface ReturnInboundQueryAdapter {
    ReturnDto getById(Long id);

    List<ReturnDto> getAll(ReturnDto filter);
}

