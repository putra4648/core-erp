package id.putra.core_erp.warehouse.module.outbound.service.adapter.command.impl;

import id.putra.core_erp.warehouse.module.outbound.dto.SalesOrderDto;
import id.putra.core_erp.warehouse.module.outbound.model.repository.SalesOrderRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.SalesOrderCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SalesOrderCommandAdapterImpl implements SalesOrderCommandAdapter {
    @Inject
    private SalesOrderRepository salesOrderRepository;

    @Override
    public void add(SalesOrderDto dto) {
    }

    @Override
    public void update(SalesOrderDto dto) {
    }

    @Override
    public void delete(Long id) {

    }
}
