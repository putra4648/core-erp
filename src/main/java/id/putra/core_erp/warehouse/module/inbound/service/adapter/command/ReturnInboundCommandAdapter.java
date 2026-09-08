package id.putra.core_erp.warehouse.module.inbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Return;

public interface ReturnInboundCommandAdapter {
    Return add(Return dto);

    Return update(Return dto);

    Boolean delete(Return dto);
}
