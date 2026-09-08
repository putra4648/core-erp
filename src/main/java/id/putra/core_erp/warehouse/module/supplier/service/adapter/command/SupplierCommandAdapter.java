package id.putra.core_erp.warehouse.module.supplier.service.adapter.command;

import java.util.List;

import id.putra.core_erp.shared.entity.Supplier;

public interface SupplierCommandAdapter {
    List<Supplier> create(List<Supplier> dto);

    void update(Long id, Supplier dto);

    void delete(Long id);
}
