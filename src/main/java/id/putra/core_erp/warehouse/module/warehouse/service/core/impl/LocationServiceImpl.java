package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.LocationDto;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.LocationCommandAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.LocationQueryAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.core.LocationCoreService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LocationServiceImpl implements LocationCoreService {

    @Inject
    private LocationQueryAdapter locationQueryAdapter;

    @Inject
    private LocationCommandAdapter locationCommandAdapter;

    @Override
    public LocationDto getLocation(LocationDto dto) {
        return locationQueryAdapter.getLocation(dto);
    }

    @Override
    public void add(List<LocationDto> dtos) {
        locationCommandAdapter.add(dtos);
    }

    @Override
    public void update(List<LocationDto> dtos) {
        locationCommandAdapter.update(dtos);
    }

    @Override
    public void delete(List<LocationDto> dtos) {
        locationCommandAdapter.delete(dtos);
    }

}
