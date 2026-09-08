package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.ContactPersonWarehouse;

public interface ContactPersonWarehouseCommandAdapter {
    void add(List<ContactPersonWarehouse> dtos);

    void update(List<ContactPersonWarehouse> dtos);

    void delete(List<ContactPersonWarehouse> dtos);
}
