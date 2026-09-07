package id.putra.core_erp.warehouse.module.customer.service.adapter.command.impl;

import id.putra.core_erp.warehouse.module.customer.dto.CustomerDto;
import id.putra.core_erp.warehouse.module.customer.model.repository.CustomerRepository;
import id.putra.core_erp.warehouse.module.customer.service.adapter.command.CustomerCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CustomerCommandAdapterImpl implements CustomerCommandAdapter {
    @Inject
    private CustomerRepository repository;

    @Override
    public CustomerDto create(CustomerDto dto) {
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public CustomerDto update(Long id, CustomerDto dto) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
}
