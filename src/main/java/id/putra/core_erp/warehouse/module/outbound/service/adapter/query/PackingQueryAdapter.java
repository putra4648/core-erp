package id.putra.core_erp.warehouse.module.outbound.service.adapter.query;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Packing;

public interface PackingQueryAdapter {
    Packing getById(Long id);

    List<Packing> getAll(Packing packing);
}
