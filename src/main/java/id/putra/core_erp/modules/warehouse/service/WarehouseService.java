package id.putra.core_erp.modules.warehouse.service;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.warehouse.model.Warehouse;
import io.quarkus.panache.common.Page;

public interface WarehouseService {

    Warehouse getWarehouse(Warehouse dto);

    void save(List<Warehouse> dtos);

    void update(List<Warehouse> dtos);

    void delete(List<Long> ids);

    long countGetWarehouses(Warehouse warehouse, Page ofSize);

    DataProvider<Warehouse, Void> getWarehouses(Warehouse param, Page ofSize);

}
