package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.WarehouseDto;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.WarehouseCommandAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.WarehouseQueryAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.core.WarehouseCoreService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class WarehouseServiceImpl implements WarehouseCoreService {

    @Inject
    private WarehouseQueryAdapter warehouseQueryAdapter;

    @Inject
    private WarehouseCommandAdapter warehouseCommandAdapter;

    @Override
    public WarehouseDto getWarehouse(WarehouseDto dto) {
        return warehouseQueryAdapter.getWarehouseById(dto);
    }

    @Override
    public void save(List<WarehouseDto> dtos) {
        warehouseCommandAdapterpersist(dtos);
    }

    @Override
    public void update(List<WarehouseDto> dtos) {
        warehouseCommandAdapter.update(dtos);
    }

    @Override
    public void delete(List<WarehouseDto> dtos) {
        warehouseCommandAdapter.delete(dtos);
    }

}
