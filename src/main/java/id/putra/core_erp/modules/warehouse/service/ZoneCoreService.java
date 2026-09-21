package id.putra.core_erp.modules.warehouse.service;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.warehouse.model.Zone;
import io.quarkus.panache.common.Page;

public interface ZoneCoreService {

    Zone getZone(Zone zoneDto);

    void add(List<Zone> dtos);

    void update(List<Zone> dtos);

    void delete(List<Zone> dtos);

    DataProvider<Zone, Void> getZones(Zone param, Page ofSize);
}
