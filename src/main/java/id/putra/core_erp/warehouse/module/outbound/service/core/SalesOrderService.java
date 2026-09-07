package id.putra.core_erp.warehouse.module.outbound.service.core;

import id.putra.core_erp.warehouse.module.outbound.dto.SalesOrderDto;

public interface SalesOrderService {
    void create(SalesOrderDto dto);

    void update(SalesOrderDto dto);

    void delete(Long id);

    SalesOrderDto getById(Long id);

}
