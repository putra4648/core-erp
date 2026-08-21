package id.putra.core_erp.warehouse.module.outbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.outbound.dto.SalesOrderDto;

public interface SalesOrderCommandAdapter {
    SalesOrderDto add(SalesOrderDto dto);

    SalesOrderDto update(SalesOrderDto dto);

    Boolean delete(Long id);
}

