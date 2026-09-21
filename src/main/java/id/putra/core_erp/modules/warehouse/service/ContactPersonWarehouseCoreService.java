package id.putra.core_erp.modules.warehouse.service;

import java.util.List;

import id.putra.core_erp.modules.warehouse.model.ContactPersonWarehouse;

public interface ContactPersonWarehouseCoreService {

    ContactPersonWarehouse getContact(ContactPersonWarehouse dto);

    void add(List<ContactPersonWarehouse> dtos);

    void update(List<ContactPersonWarehouse> dtos);

    void delete(List<ContactPersonWarehouse> dtos);
}
