package id.putra.core_erp.warehouse.module.supplier.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.supplier.dto.SupplierDto;
import id.putra.core_erp.warehouse.module.supplier.service.adapter.command.SupplierCommandAdapter;
import id.putra.core_erp.warehouse.module.supplier.service.adapter.query.SupplierQueryAdapter;
import id.putra.core_erp.warehouse.module.supplier.service.core.SupplierService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SupplierServiceImpl implements SupplierService {
    @Inject
    private SupplierCommandAdapter supplierCommandAdapter;

    @Inject
    private SupplierQueryAdapter supplierQueryAdapter;

    @Override
    public SupplierDto getById(Long id) {
        SupplierDto entity = supplierQueryAdapter.getById(id);
        return entity;
    }

    @Override
    public List<SupplierDto> create(List<SupplierDto> dto) {
        return supplierCommandAdapter.create(dto);
    }

    @Override
    public void update(Long id, SupplierDto dto) {
        supplierCommandAdapter.update(id, dto);
    }

    @Override
    public void delete(Long id) {
        supplierCommandAdapter.delete(id);
    }
}
