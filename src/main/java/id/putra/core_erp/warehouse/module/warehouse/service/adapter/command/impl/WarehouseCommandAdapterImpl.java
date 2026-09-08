package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Warehouse;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.WarehouseRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.WarehouseCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class WarehouseCommandAdapterImpl implements WarehouseCommandAdapter {

    @Inject
    private WarehouseRepository warehouseRepository;

    @Override
    public void save(List<Warehouse> dtos) {
    }

    @Override
    public void update(List<Warehouse> dtos) {
    }

    @Override
    public void delete(List<Long> ids) {
    }

}
