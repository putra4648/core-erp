package id.putra.core_erp.warehouse.module.supplier.service.core;

import java.util.List;

import id.putra.core_erp.warehouse.module.supplier.dto.SupplierDto;

public interface SupplierService {

    SupplierDto getById(Long id);

    List<SupplierDto> create(List<SupplierDto> dto);

    void update(Long id, SupplierDto dto);

    void delete(Long id);
}
