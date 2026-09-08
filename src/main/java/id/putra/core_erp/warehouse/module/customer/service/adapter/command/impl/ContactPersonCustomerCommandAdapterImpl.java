package id.putra.core_erp.warehouse.module.customer.service.adapter.command.impl;

import id.putra.core_erp.shared.entity.ContactPersonCustomer;
import id.putra.core_erp.warehouse.module.customer.model.repository.ContactPersonCustomerRepository;
import id.putra.core_erp.warehouse.module.customer.service.adapter.command.ContactPersonCustomerCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ContactPersonCustomerCommandAdapterImpl implements ContactPersonCustomerCommandAdapter {
    @Inject
    private ContactPersonCustomerRepository repository;

    @Override
    public ContactPersonCustomer create(ContactPersonCustomer dto) {
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public ContactPersonCustomer update(Long id, ContactPersonCustomer dto) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
}
