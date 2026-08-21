package id.putra.core_erp.warehouse.module.inbound.service.adapter.command.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.putra.core_erp.warehouse.module.inbound.dto.LabellingDto;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.LabellingCommandAdapter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class LabellingCommandAdapterImpl implements LabellingCommandAdapter {

    @Override
    public LabellingDto add(LabellingDto dto) {
        // TODO: Placeholder - implement when repository is ready
        return dto;
    }

    @Override
    public LabellingDto update(LabellingDto dto) {
        // TODO: Placeholder - implement when repository is ready
        return dto;
    }

    @Override
    public Boolean delete(LabellingDto dto) {
        // TODO: Placeholder - implement when repository is ready
        return false;
    }
}

