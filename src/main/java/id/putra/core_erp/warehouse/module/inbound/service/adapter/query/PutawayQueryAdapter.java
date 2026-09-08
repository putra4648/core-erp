package id.putra.core_erp.warehouse.module.inbound.service.adapter.query;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Putaway;

public interface PutawayQueryAdapter {
    Putaway getById(Long id);

    List<Putaway> getAll(Putaway filter);
}
