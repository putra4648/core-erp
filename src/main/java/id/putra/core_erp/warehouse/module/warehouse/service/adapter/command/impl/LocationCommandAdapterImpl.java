package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Location;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.LocationRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.LocationCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LocationCommandAdapterImpl implements LocationCommandAdapter {

    @Inject
    private LocationRepository locationRepository;

    @Override
    public void add(List<Location> dtos) {

    }

    @Override
    public void update(List<Location> dtos) {
    }

    @Override
    public void delete(List<Location> dtos) {
    }

}
