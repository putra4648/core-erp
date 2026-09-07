package id.putra.core_erp.warehouse.module.outbound.service.adapter.query;

import id.putra.core_erp.warehouse.module.outbound.dto.SalesOrderDto;

public interface SalesOrderQueryAdapter {
    SalesOrderDto getById(Long id);

}
