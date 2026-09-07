package id.putra.core_erp.warehouse.module.warehouse.service.core;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.ContactPersonWarehouseDto;

public interface ContactPersonWarehouseCoreService {

    ContactPersonWarehouseDto getContact(ContactPersonWarehouseDto dto);

    void add(List<ContactPersonWarehouseDto> dtos);

    void update(List<ContactPersonWarehouseDto> dtos);

    void delete(List<ContactPersonWarehouseDto> dtos);
}
