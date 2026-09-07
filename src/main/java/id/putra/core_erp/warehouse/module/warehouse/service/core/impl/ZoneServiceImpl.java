package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.dto.ZoneDto;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.ZoneCommandAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.ZoneQueryAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.core.ZoneCoreService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ZoneServiceImpl implements ZoneCoreService {

    @Inject
    private ZoneQueryAdapter zoneQueryAdapter;

    @Inject
    private ZoneCommandAdapter zoneCommandAdapter;

    @Override
    public ZoneDto getZone(ZoneDto zoneDto) {
        return zoneQueryAdapter.getZone(zoneDto);
    }

    @Override
    public void add(List<ZoneDto> dtos) {
        zoneCommandAdapter.add(dtos);
    }

    @Override
    public void update(List<ZoneDto> dtos) {
        zoneCommandAdapter.update(dtos);
    }

    @Override
    public void delete(List<ZoneDto> dtos) {
        zoneCommandAdapter.delete(dtos);
    }

}
