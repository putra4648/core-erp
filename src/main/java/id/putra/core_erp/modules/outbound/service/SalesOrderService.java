package id.putra.core_erp.modules.outbound.service;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.outbound.model.SalesOrder;
import io.quarkus.panache.common.Page;

public interface SalesOrderService {
    void create(SalesOrder dto);

    void update(SalesOrder dto);

    void delete(Long id);

    SalesOrder getById(Long id);

    DataProvider<SalesOrder, Void> getAll(String value, Page ofSize);

}
