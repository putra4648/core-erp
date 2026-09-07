package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.WarehouseDto;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.WarehouseRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.WarehouseCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class WarehouseCommandAdapterImpl implements WarehouseCommandAdapter {

    @Inject
    private WarehouseRepository warehouseRepository;

    @Override
    public void save(List<WarehouseDto> dtos) {
    }

    @Override
    public void update(List<WarehouseDto> dtos) {
    }

    @Override
    public void delete(List<WarehouseDto> dtos) {
    }

}
