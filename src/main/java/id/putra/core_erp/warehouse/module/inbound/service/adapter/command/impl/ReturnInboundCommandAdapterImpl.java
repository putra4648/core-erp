package id.putra.core_erp.warehouse.module.inbound.service.adapter.command.impl;

import id.putra.core_erp.warehouse.module.inbound.dto.ReturnDto;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.ReturnInboundCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReturnInboundCommandAdapterImpl implements ReturnInboundCommandAdapter {

    @Override
    public ReturnDto add(ReturnDto dto) {
        // TODO: Placeholder - implement when repository is ready
        return dto;
    }

    @Override
    public ReturnDto update(ReturnDto dto) {
        // TODO: Placeholder - implement when repository is ready
        return dto;
    }

    @Override
    public Boolean delete(ReturnDto dto) {
        // TODO: Placeholder - implement when repository is ready
        return false;
    }
}
