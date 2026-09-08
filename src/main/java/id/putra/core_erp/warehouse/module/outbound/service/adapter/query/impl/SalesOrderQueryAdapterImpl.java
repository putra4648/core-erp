package id.putra.core_erp.warehouse.module.outbound.service.adapter.query.impl;

import id.putra.core_erp.warehouse.module.outbound.model.entity.SalesOrder;
import id.putra.core_erp.warehouse.module.outbound.model.repository.SalesOrderRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.SalesOrderQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SalesOrderQueryAdapterImpl implements SalesOrderQueryAdapter {
    @Inject
    private SalesOrderRepository salesOrderRepository;

    @Override
    public SalesOrder getById(Long id) {
        return null;
    }

}
