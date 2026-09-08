package id.putra.core_erp.warehouse.module.outbound.service.adapter.command.impl;

import id.putra.core_erp.warehouse.module.outbound.model.entity.SalesOrder;
import id.putra.core_erp.warehouse.module.outbound.model.repository.SalesOrderRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.SalesOrderCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SalesOrderCommandAdapterImpl implements SalesOrderCommandAdapter {
    @Inject
    private SalesOrderRepository salesOrderRepository;

    @Override
    public void add(SalesOrder dto) {
    }

    @Override
    public void update(SalesOrder dto) {
    }

    @Override
    public void delete(Long id) {

    }
}
