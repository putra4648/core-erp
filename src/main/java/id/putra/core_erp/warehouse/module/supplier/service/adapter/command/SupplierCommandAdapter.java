package id.putra.core_erp.warehouse.module.supplier.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.supplier.dto.SupplierDto;

public interface SupplierCommandAdapter {
    List<SupplierDto> create(List<SupplierDto> dto);

    SupplierDto update(Long id, SupplierDto dto);

    void delete(Long id);
}

