package id.putra.core_erp.warehouse.module.inbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Receiving;

public interface ReceivingCommandAdapter {
    Receiving add(Receiving dto);

    Receiving update(Receiving dto);

    Boolean delete(Long id);

}
