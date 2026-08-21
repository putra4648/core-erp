package id.putra.core_erp.warehouse.module.warehouse.service.adapter.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.warehouse.dto.RackDto;

public interface RackQueryAdapter {
    Page<RackDto> getRacks(RackDto dto, Pageable pageable);

    RackDto getRackById(RackDto rackDto);

}

