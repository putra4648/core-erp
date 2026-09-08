package id.putra.core_erp.warehouse.module.inbound.service.adapter.command.impl;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Return;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.ReturnInboundCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReturnInboundCommandAdapterImpl implements ReturnInboundCommandAdapter {

    @Override
    public Return add(Return dto) {
        // TODO: Placeholder - implement when repository is ready
        return dto;
    }

    @Override
    public Return update(Return dto) {
        // TODO: Placeholder - implement when repository is ready
        return dto;
    }

    @Override
    public Boolean delete(Return dto) {
        // TODO: Placeholder - implement when repository is ready
        return false;
    }
}
