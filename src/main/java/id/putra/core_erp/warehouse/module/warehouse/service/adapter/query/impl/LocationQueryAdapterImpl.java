package id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import id.putra.core_erp.warehouse.module.warehouse.converter.LocationConverter;
import id.putra.core_erp.warehouse.module.warehouse.dto.LocationDto;
import id.putra.core_erp.warehouse.module.warehouse.model.entity.Location;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.LocationRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.LocationQueryAdapter;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationQueryAdapterImpl implements LocationQueryAdapter {
    private final LocationRepository locationRepository;

    @Override
    public Page<LocationDto> getLocations(LocationDto dto, Pageable pageable) {
        Specification<Location> specs = (root, criteria, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dto != null) {
                if (StringUtils.hasText(dto.getName())) {
                    predicates.add(builder.like(root.get("name"),
                            "%" + dto.getName() + "%"));
                }
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
        Pageable safePageable = Objects.requireNonNull(pageable);
        return locationRepository.findAll(specs, safePageable).map(LocationConverter::toDto);
    }

    @Override
    public LocationDto getLocation(LocationDto dto) {
        Long safeId = Objects.requireNonNull(dto.getId());
        return locationRepository.findById(safeId).map(LocationConverter::toDto).orElse(null);
    }

}

