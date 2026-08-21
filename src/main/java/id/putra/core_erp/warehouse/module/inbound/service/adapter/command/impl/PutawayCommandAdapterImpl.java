package id.putra.core_erp.warehouse.module.inbound.service.adapter.command.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.putra.core_erp.warehouse.module.inbound.dto.PutawayDto;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.PutawayCommandAdapter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
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

