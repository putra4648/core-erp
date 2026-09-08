package id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.impl;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.ContactPersonWarehouse;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.ContactPersonWarehouseRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.ContactPersonWarehouseQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ContactPersonnWarehouseQueryAdapterImpl implements ContactPersonWarehouseQueryAdapter {

    @Inject
    private ContactPersonWarehouseRepository contactPersonWarehouseRepository;

    @Override
    public ContactPersonWarehouse getContact(ContactPersonWarehouse dto) {

        var cp = contactPersonWarehouseRepository.findById(dto.getId());
        return null;
    }

}
