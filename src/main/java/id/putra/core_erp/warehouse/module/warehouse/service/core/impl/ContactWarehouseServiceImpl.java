package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.ContactPersonWarehouse;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.ContactPersonWarehouseCommandAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.ContactPersonWarehouseQueryAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.core.ContactPersonWarehouseCoreService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ContactWarehouseServiceImpl implements ContactPersonWarehouseCoreService {
    @Inject
    private ContactPersonWarehouseQueryAdapter contactPersonWarehouseQueryAdapter;

    @Inject
    private ContactPersonWarehouseCommandAdapter contactPersonCommandAdapter;

    @Override
    public ContactPersonWarehouse getContact(ContactPersonWarehouse dto) {
        return contactPersonWarehouseQueryAdapter.getContact(dto);
    }

    @Override
    public void add(List<ContactPersonWarehouse> dtos) {
        contactPersonCommandAdapter.add(dtos);
    }

    @Override
    public void update(List<ContactPersonWarehouse> dtos) {
        contactPersonCommandAdapter.update(dtos);
    }

    @Override
    public void delete(List<ContactPersonWarehouse> dtos) {
        contactPersonCommandAdapter.delete(dtos);
    }

}
