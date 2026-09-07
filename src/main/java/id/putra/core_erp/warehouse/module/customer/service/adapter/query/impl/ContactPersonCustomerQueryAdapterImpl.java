package id.putra.core_erp.warehouse.module.customer.service.adapter.query.impl;

import id.putra.core_erp.warehouse.module.customer.dto.ContactPersonCustomerDto;
import id.putra.core_erp.warehouse.module.customer.model.repository.ContactPersonCustomerRepository;
import id.putra.core_erp.warehouse.module.customer.service.adapter.query.ContactPersonCustomerQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ContactPersonCustomerQueryAdapterImpl implements ContactPersonCustomerQueryAdapter {
    @Inject
    private ContactPersonCustomerRepository repository;

    @Override
    public ContactPersonCustomerDto getById(Long id) {
        return null;
    }
}
