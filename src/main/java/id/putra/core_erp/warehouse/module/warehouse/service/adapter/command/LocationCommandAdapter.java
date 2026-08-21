package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.LocationDto;

public interface LocationCommandAdapter {
    void add(List<LocationDto> dtos);

    void update(List<LocationDto> dtos);

    void delete(List<LocationDto> dtos);
}

