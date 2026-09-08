package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Zone;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.command.ZoneCommandAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.ZoneQueryAdapter;
import id.putra.core_erp.warehouse.module.warehouse.service.core.ZoneCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ZoneServiceImpl implements ZoneCoreService {

    @Inject
    private ZoneQueryAdapter zoneQueryAdapter;

    @Inject
    private ZoneCommandAdapter zoneCommandAdapter;

    @Override
    public Zone getZone(Zone zoneDto) {
        return zoneQueryAdapter.getZone(zoneDto);
    }

    @Override
    public void add(List<Zone> dtos) {
        zoneCommandAdapter.add(dtos);
    }

    @Override
    public void update(List<Zone> dtos) {
        zoneCommandAdapter.update(dtos);
    }

    @Override
    public void delete(List<Zone> dtos) {
        zoneCommandAdapter.delete(dtos);
    }

    @Override
    public DataProvider<Zone, Void> getZones(Zone param, Page ofSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getZones'");
    }

}
