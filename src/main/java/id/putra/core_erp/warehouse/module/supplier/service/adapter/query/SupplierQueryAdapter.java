package id.putra.core_erp.warehouse.module.supplier.service.adapter.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.supplier.dto.SupplierDto;

public interface SupplierQueryAdapter {
    Page<SupplierDto> getAll(Pageable pageable, String search);

    SupplierDto getById(Long id);

}

