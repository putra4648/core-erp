package id.putra.core_erp.warehouse.module.supplier.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.shared.entity.Supplier;
import id.putra.core_erp.warehouse.module.supplier.model.repository.SupplierRepository;
import id.putra.core_erp.warehouse.module.supplier.service.adapter.command.SupplierCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SupplierCommandAdapterImpl implements SupplierCommandAdapter {
    @Inject
    private SupplierRepository repository;

    @Override
    public List<Supplier> create(List<Supplier> dto) {
        return null;
    }

    @Override
    public void update(Long id, Supplier dto) {

    }

    @Override
    public void delete(Long id) {
    }
}
