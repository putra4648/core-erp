package id.putra.core_erp.warehouse.module.inbound.service.adapter.command.impl;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Putaway;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.PutawayCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PutawayCommandAdapterImpl implements PutawayCommandAdapter {

    @Override
    public Putaway add(Putaway dto) {
        // TODO: Placeholder - implement when repository is ready
        return dto;
    }

    @Override
    public Putaway update(Putaway dto) {
        // TODO: Placeholder - implement when repository is ready
        return dto;
    }

    @Override
    public Boolean delete(Putaway dto) {
        // TODO: Placeholder - implement when repository is ready
        return false;
    }
}
