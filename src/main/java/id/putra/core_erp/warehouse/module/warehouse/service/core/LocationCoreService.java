package id.putra.core_erp.warehouse.module.warehouse.service.core;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.LocationDto;

public interface LocationCoreService {

    LocationDto getLocation(LocationDto dto);

    void add(List<LocationDto> dtos);

    void update(List<LocationDto> dtos);

    void delete(List<LocationDto> dtos);
}
