package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.ContactPersonWarehouseDto;

public interface ContactPersonWarehouseCommandAdapter {
    void add(List<ContactPersonWarehouseDto> dtos);

    void update(List<ContactPersonWarehouseDto> dtos);

    void delete(List<ContactPersonWarehouseDto> dtos);
}

