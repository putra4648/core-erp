package id.putra.core_erp.warehouse.module.supplier.service.adapter.query.impl;

import id.putra.core_erp.warehouse.module.supplier.dto.SupplierDto;
import id.putra.core_erp.warehouse.module.supplier.model.repository.SupplierRepository;
import id.putra.core_erp.warehouse.module.supplier.service.adapter.query.SupplierQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SupplierQueryAdapterImpl implements SupplierQueryAdapter {
    @Inject
    private SupplierRepository repository;

    @Override
    public SupplierDto getById(Long id) {
        return null;
    }
}
