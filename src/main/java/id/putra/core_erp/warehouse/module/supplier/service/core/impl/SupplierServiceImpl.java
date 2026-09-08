package id.putra.core_erp.warehouse.module.supplier.service.core.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.shared.entity.Supplier;
import id.putra.core_erp.warehouse.module.supplier.service.adapter.command.SupplierCommandAdapter;
import id.putra.core_erp.warehouse.module.supplier.service.adapter.query.SupplierQueryAdapter;
import id.putra.core_erp.warehouse.module.supplier.service.core.SupplierService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SupplierServiceImpl implements SupplierService {
    @Inject
    private SupplierCommandAdapter supplierCommandAdapter;

    @Inject
    private SupplierQueryAdapter supplierQueryAdapter;

    @Override
    public Supplier getById(Long id) {
        Supplier entity = supplierQueryAdapter.getById(id);
        return entity;
    }

    @Override
    public List<Supplier> create(List<Supplier> dto) {
        return supplierCommandAdapter.create(dto);
    }

    @Override
    public void update(Long id, Supplier dto) {
        supplierCommandAdapter.update(id, dto);
    }

    @Override
    public void delete(Long id) {
        supplierCommandAdapter.delete(id);
    }

    @Override
    public DataProvider<Supplier, Void> getAll(String value, Page ofSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }
}
