package id.putra.core_erp.warehouse.module.supplier.service.adapter.query.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import id.putra.core_erp.warehouse.module.supplier.converter.SupplierConverter;
import id.putra.core_erp.warehouse.module.supplier.dto.SupplierDto;
import id.putra.core_erp.warehouse.module.supplier.model.repository.SupplierRepository;
import id.putra.core_erp.warehouse.module.supplier.service.adapter.query.SupplierQueryAdapter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierQueryAdapterImpl implements SupplierQueryAdapter {
    private final SupplierRepository repository;

    @Override
    public Page<SupplierDto> getAll(Pageable pageable, String search) {
        Pageable safePageable = java.util.Objects.requireNonNull(pageable);
        return repository.findAll(safePageable).map(SupplierConverter::toDto);
    }

    @Override
    public SupplierDto getById(Long id) {
        Long safeId = java.util.Objects.requireNonNull(id);
        var entity = repository.findById(String.valueOf(safeId)).orElseThrow();
        return SupplierConverter.toDto(entity);
    }
}

