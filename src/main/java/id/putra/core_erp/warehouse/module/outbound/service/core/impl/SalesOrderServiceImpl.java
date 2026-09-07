package id.putra.core_erp.warehouse.module.outbound.service.core.impl;

import id.putra.core_erp.warehouse.module.outbound.dto.SalesOrderDto;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.SalesOrderCommandAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.SalesOrderQueryAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.core.SalesOrderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SalesOrderServiceImpl implements SalesOrderService {
    @Inject
    private SalesOrderCommandAdapter salesOrderCommandAdapter;
    @Inject
    private SalesOrderQueryAdapter salesOrderQueryAdapter;

    @Override
    public void create(SalesOrderDto dto) {

    }

    @Override
    public void update(SalesOrderDto dto) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public SalesOrderDto getById(Long id) {
        return salesOrderQueryAdapter.getById(id);
    }

}
