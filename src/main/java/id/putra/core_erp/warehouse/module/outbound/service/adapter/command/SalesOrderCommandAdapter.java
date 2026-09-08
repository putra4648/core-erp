package id.putra.core_erp.warehouse.module.outbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.outbound.model.entity.SalesOrder;

public interface SalesOrderCommandAdapter {
    void add(SalesOrder dto);

    void update(SalesOrder dto);

    void delete(Long id);
}
