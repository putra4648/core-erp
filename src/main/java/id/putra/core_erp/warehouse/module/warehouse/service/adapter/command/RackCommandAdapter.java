package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.RackDto;

public interface RackCommandAdapter {
    void add(List<RackDto> dtos);

    void update(List<RackDto> dtos);

    void delete(List<RackDto> dtos);
}

