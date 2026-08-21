package id.putra.core_erp.warehouse.module.warehouse.service.adapter.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.warehouse.dto.WarehouseDto;

public interface WarehouseQueryAdapter {

    Page<WarehouseDto> getWarehouses(WarehouseDto dto, Pageable pageable);

    WarehouseDto getWarehouseById(WarehouseDto dto);

}

