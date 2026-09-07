package id.putra.core_erp.warehouse.module.warehouse.service.core;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.ZoneDto;

public interface ZoneCoreService {

    ZoneDto getZone(ZoneDto zoneDto);

    void add(List<ZoneDto> dtos);

    void update(List<ZoneDto> dtos);

    void delete(List<ZoneDto> dtos);
}
