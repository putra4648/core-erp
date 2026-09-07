package id.putra.core_erp.warehouse.module.warehouse.service.core;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.RackDto;

public interface RackCoreService {

    RackDto getRack(RackDto rackDto);

    void add(List<RackDto> dtos);

    void update(List<RackDto> dtos);

    void delete(List<RackDto> dtos);
}
