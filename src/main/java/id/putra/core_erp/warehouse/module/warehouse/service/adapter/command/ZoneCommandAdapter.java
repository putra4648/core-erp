package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Zone;

public interface ZoneCommandAdapter {
    void add(List<Zone> dtos);

    void update(List<Zone> dtos);

    void delete(List<Zone> dtos);
}
