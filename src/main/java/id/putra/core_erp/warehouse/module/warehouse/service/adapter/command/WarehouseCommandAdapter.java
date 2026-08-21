package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.WarehouseDto;

public interface WarehouseCommandAdapter {

    void save(List<WarehouseDto> dtos);

    void update(List<WarehouseDto> dtos);

    void delete(List<WarehouseDto> dtos);

}

