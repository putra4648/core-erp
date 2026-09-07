package id.putra.core_erp.warehouse.module.customer.service.adapter.query.impl;

import id.putra.core_erp.warehouse.module.customer.dto.CustomerDto;
import id.putra.core_erp.warehouse.module.customer.model.repository.CustomerRepository;
import id.putra.core_erp.warehouse.module.customer.service.adapter.query.CustomerQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CustomerQueryAdapterImpl implements CustomerQueryAdapter {
    @Inject
    private CustomerRepository repository;

    @Override
    public CustomerDto getById(Long id) {
        return null;
    }
}
