package id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.impl;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import id.putra.core_erp.warehouse.module.warehouse.converter.WarehouseConverter;
import id.putra.core_erp.warehouse.module.warehouse.dto.WarehouseDto;
import id.putra.core_erp.warehouse.module.warehouse.model.entity.Warehouse;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.WarehouseRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.WarehouseQueryAdapter;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseQueryAdapterImpl implements WarehouseQueryAdapter {

    private final WarehouseRepository warehouseRepository;

    @Override
    public WarehouseDto getWarehouseById(WarehouseDto dto) {
        Long id = Objects.requireNonNull(dto.getId());
        var wh = warehouseRepository.findById(id);
        return wh.map(WarehouseConverter::toDto).orElse(null);
    }

    @Override
    public Page<WarehouseDto> getWarehouses(WarehouseDto dto, Pageable pageable) {
        Specification<Warehouse> specs = (root, criteria, builder) -> {
            Predicate name = builder.like(root.get("name"),
                    "%" + (StringUtils.hasText(dto.getName()) ? dto.getName() : "") + "%");
            return builder.and(name);
        };
        Pageable safePageable = Objects.requireNonNull(pageable);
        return warehouseRepository.findAll(specs, safePageable).map(WarehouseConverter::toDto);
    }

}

