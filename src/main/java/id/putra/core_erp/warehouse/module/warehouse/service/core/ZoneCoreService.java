package id.putra.core_erp.warehouse.module.warehouse.service.core;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Zone;
import io.quarkus.panache.common.Page;

public interface ZoneCoreService {

    Zone getZone(Zone zoneDto);

    void add(List<Zone> dtos);

    void update(List<Zone> dtos);

    void delete(List<Zone> dtos);

    DataProvider<Zone, Void> getZones(Zone param, Page ofSize);
}
