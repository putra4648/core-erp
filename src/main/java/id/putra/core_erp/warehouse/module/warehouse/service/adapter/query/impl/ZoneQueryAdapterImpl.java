package id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.impl;

import id.putra.core_erp.warehouse.module.warehouse.dto.ZoneDto;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.ZoneRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.ZoneQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ZoneQueryAdapterImpl implements ZoneQueryAdapter {

    @Inject
    private ZoneRepository zoneRepository;

    @Override
    public ZoneDto getZone(ZoneDto dto) {
        var z = zoneRepository.findById(dto.getId());
        return null;
    }
}