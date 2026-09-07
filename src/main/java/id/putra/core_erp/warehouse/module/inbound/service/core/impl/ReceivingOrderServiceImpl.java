package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import org.jspecify.annotations.NonNull;

import id.putra.core_erp.warehouse.module.inbound.dto.ReceivingDto;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.ReceivingCommandAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.ReceivingQueryAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.core.ReceivingOrderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReceivingOrderServiceImpl implements ReceivingOrderService {

    @Inject
    private ReceivingCommandAdapter receivingCommandAdapter;

    @Inject
    private ReceivingQueryAdapter receivingQueryAdapter;

    @Override
    public ReceivingDto create(ReceivingDto dto) {
        return receivingCommandAdapter.add(dto);
    }

    @Override
    public ReceivingDto update(ReceivingDto dto) {
        return receivingCommandAdapter.update(dto);
    }

    @Override
    public Boolean delete(@NonNull Long id) {
        return receivingCommandAdapter.delete(id);
    }

}
