package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Location;

public interface LocationCommandAdapter {
    void add(List<Location> dtos);

    void update(List<Location> dtos);

    void delete(List<Location> dtos);
}
