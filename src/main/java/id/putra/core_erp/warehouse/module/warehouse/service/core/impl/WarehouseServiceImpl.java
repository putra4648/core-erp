package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import id.putra.core_erp.warehouse.module.warehouse.dto.WarehouseDto;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.WarehouseCommandAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.WarehouseQueryAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.core.WarehouseCoreService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseCoreService {
    private final WarehouseQueryAdapter warehouseQueryAdapter;
    private final WarehouseCommandAdapter warehouseCommandAdapter;

    @Override
    public Page<WarehouseDto> getWarehouses(WarehouseDto dto, Pageable pageable) {
        return warehouseQueryAdapter.getWarehouses(dto, pageable);
    }

    @Override
    public WarehouseDto getWarehouse(WarehouseDto dto) {
        return warehouseQueryAdapter.getWarehouseById(dto);
    }

    @Override
    public void save(List<WarehouseDto> dtos) {
        warehouseCommandAdapter.save(dtos);
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

