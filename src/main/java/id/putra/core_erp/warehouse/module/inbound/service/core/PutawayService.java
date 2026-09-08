package id.putra.core_erp.warehouse.module.inbound.service.core;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Putaway;

public interface PutawayService {
    Putaway create(Putaway dto);

    Putaway update(Putaway dto);

    Boolean delete(Long id);

    Putaway getById(Long id);

    List<Putaway> getAll(Putaway filter);
}
