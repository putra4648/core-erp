package id.putra.core_erp.warehouse.module.outbound.service.core.impl;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.outbound.model.entity.SalesOrder;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.SalesOrderCommandAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.SalesOrderQueryAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.core.SalesOrderService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SalesOrderServiceImpl implements SalesOrderService {
    @Inject
    private SalesOrderCommandAdapter salesOrderCommandAdapter;
    @Inject
    private SalesOrderQueryAdapter salesOrderQueryAdapter;

    @Override
    public void create(SalesOrder dto) {

    }

    @Override
    public void update(SalesOrder dto) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public SalesOrder getById(Long id) {
        return salesOrderQueryAdapter.getById(id);
    }

    @Override
    public DataProvider<SalesOrder, Void> getAll(String value, Page ofSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

}
