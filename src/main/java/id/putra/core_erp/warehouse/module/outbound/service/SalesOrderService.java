package id.putra.core_erp.warehouse.module.outbound.service;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.outbound.model.entity.SalesOrder;
import io.quarkus.panache.common.Page;

public interface SalesOrderService {
    void create(SalesOrder dto);

    void update(SalesOrder dto);

    void delete(Long id);

    SalesOrder getById(Long id);

    DataProvider<SalesOrder, Void> getAll(String value, Page ofSize);

}
