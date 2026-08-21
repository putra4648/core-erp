package id.putra.core_erp.warehouse.module.warehouse.service.adapter.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.warehouse.dto.LocationDto;

public interface LocationQueryAdapter {
    Page<LocationDto> getLocations(LocationDto dto, Pageable pageable);

    LocationDto getLocation(LocationDto dto);
}

