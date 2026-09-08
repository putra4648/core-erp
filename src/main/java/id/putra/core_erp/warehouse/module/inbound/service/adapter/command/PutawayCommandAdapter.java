package id.putra.core_erp.warehouse.module.inbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Putaway;

public interface PutawayCommandAdapter {
    Putaway add(Putaway dto);

    Putaway update(Putaway dto);

    Boolean delete(Putaway dto);
}
