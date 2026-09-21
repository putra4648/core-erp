package id.putra.core_erp.modules.warehouse.service;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.warehouse.model.Location;
import io.quarkus.panache.common.Page;

public interface LocationCoreService {

    Location getLocation(Location dto);

    void add(List<Location> dtos);

    void update(List<Location> dtos);

    void delete(List<Location> dtos);

    DataProvider<Location, Void> getLocations(Location param, Page ofSize);
}
