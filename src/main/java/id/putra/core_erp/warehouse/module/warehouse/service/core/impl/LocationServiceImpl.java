package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Location;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.LocationCommandAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.LocationQueryAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.core.LocationCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LocationServiceImpl implements LocationCoreService {

    @Inject
    private LocationQueryAdapter locationQueryAdapter;

    @Inject
    private LocationCommandAdapter locationCommandAdapter;

    @Override
    public Location getLocation(Location dto) {
        return locationQueryAdapter.getLocation(dto);
    }

    @Override
    public void add(List<Location> dtos) {
        locationCommandAdapter.add(dtos);
    }

    @Override
    public void update(List<Location> dtos) {
        locationCommandAdapter.update(dtos);
    }

    @Override
    public void delete(List<Location> dtos) {
        locationCommandAdapter.delete(dtos);
    }

    @Override
    public DataProvider<Location, Void> getLocations(Location param, Page ofSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLocations'");
    }

}
