package id.putra.core_erp.warehouse.module.inbound.service.adapter.command.impl;

import id.putra.core_erp.warehouse.module.inbound.dto.PutawayDto;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.PutawayCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PutawayCommandAdapterImpl implements PutawayCommandAdapter {

    @Override
    public PutawayDto add(PutawayDto dto) {
        // TODO: Placeholder - implement when repository is ready
        return dto;
    }

    @Override
    public PutawayDto update(PutawayDto dto) {
        // TODO: Placeholder - implement when repository is ready
        return dto;
    }

    @Override
    public Boolean delete(PutawayDto dto) {
        // TODO: Placeholder - implement when repository is ready
        return false;
    }
}
