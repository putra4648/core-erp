package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.ContactPersonWarehouseDto;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.ContactPersonWarehouseRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.ContactPersonWarehouseCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ContactPersonWarehouseCommandAdapterImpl implements ContactPersonWarehouseCommandAdapter {

    @Inject
    private ContactPersonWarehouseRepository contactPersonWarehouseRepository;

    @Override
    public void add(List<ContactPersonWarehouseDto> dtos) {

    }

    @Override
    public void update(List<ContactPersonWarehouseDto> dtos) {

    }

    @Override
    public void delete(List<ContactPersonWarehouseDto> dtos) {
    }

}
