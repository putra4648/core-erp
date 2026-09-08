package id.putra.core_erp.warehouse.module.outbound.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Packing;

public interface PackingCommandAdapter {
    void add(List<Packing> dtos);

    void update(List<Packing> dtos);

    void delete(List<Long> ids);
}
