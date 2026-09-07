package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.ContactPersonWarehouseDto;
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
    public ContactPersonWarehouseDto getContact(ContactPersonWarehouseDto dto) {
        return contactPersonWarehouseQueryAdapter.getContact(dto);
    }

    @Override
    public void add(List<ContactPersonWarehouseDto> dtos) {
        contactPersonCommandAdapter.add(dtos);
    }

    @Override
    public void update(List<ContactPersonWarehouseDto> dtos) {
        contactPersonCommandAdapter.update(dtos);
    }

    @Override
    public void delete(List<ContactPersonWarehouseDto> dtos) {
        contactPersonCommandAdapter.delete(dtos);
    }

}
