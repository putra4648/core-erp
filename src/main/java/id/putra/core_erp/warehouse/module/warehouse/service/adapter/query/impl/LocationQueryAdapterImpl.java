package id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.impl;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Location;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.LocationRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.LocationQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LocationQueryAdapterImpl implements LocationQueryAdapter {

    @Inject
    private LocationRepository locationRepository;

    @Override
    public Location getLocation(Location dto) {
        var l = locationRepository.findById(dto.getId());
        return null;
    }

}
