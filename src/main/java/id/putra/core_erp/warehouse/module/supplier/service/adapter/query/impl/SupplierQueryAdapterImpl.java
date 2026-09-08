package id.putra.core_erp.warehouse.module.supplier.service.adapter.query.impl;

import id.putra.core_erp.shared.entity.Supplier;
import id.putra.core_erp.warehouse.module.supplier.model.repository.SupplierRepository;
import id.putra.core_erp.warehouse.module.supplier.service.adapter.query.SupplierQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SupplierQueryAdapterImpl implements SupplierQueryAdapter {
    @Inject
    private SupplierRepository repository;

    @Override
    public Supplier getById(Long id) {
        return repository.findById(id);
    }
}
