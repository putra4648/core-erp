package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Warehouse;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.WarehouseCommandAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.WarehouseQueryAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.core.WarehouseCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class WarehouseServiceImpl implements WarehouseCoreService {

    @Inject
    private WarehouseQueryAdapter warehouseQueryAdapter;

    @Inject
    private WarehouseCommandAdapter warehouseCommandAdapter;

    @Override
    public Warehouse getWarehouse(Warehouse dto) {
        return warehouseQueryAdapter.getWarehouseById(dto);
    }

    @Override
    public void save(List<Warehouse> dtos) {
        warehouseCommandAdapter.save(dtos);
    }

    @Override
    public void update(List<Warehouse> dtos) {
        warehouseCommandAdapter.update(dtos);
    }

    @Override
    public void delete(List<Long> ids) {
        warehouseCommandAdapter.delete(ids);
    }

    @Override
    public long countGetWarehouses(Warehouse warehouse, Page ofSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countGetWarehouses'");
    }

    @Override
    public DataProvider<Warehouse, Void> getWarehouses(Warehouse param, Page ofSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarehouses'");
    }

}
