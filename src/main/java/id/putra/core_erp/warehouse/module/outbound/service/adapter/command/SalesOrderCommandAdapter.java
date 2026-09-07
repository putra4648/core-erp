package id.putra.core_erp.warehouse.module.outbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.outbound.dto.SalesOrderDto;

public interface SalesOrderCommandAdapter {
    void add(SalesOrderDto dto);

    void update(SalesOrderDto dto);

    void delete(Long id);
}
