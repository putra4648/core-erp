package id.putra.core_erp.warehouse.module.outbound.service.adapter.query;

import id.putra.core_erp.warehouse.module.outbound.model.entity.SalesOrder;

public interface SalesOrderQueryAdapter {
    SalesOrder getById(Long id);

}
