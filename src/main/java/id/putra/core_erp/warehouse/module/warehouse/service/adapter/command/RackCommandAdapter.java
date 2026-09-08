package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Rack;

public interface RackCommandAdapter {
    void add(List<Rack> dtos);

    void update(List<Rack> dtos);

    void delete(List<Rack> dtos);
}
