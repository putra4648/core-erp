package id.putra.core_erp.warehouse.module.inbound.service.adapter.command.impl;

import id.putra.core_erp.warehouse.module.inbound.dto.ReceivingDto;
import id.putra.core_erp.warehouse.module.inbound.model.repository.PurchaseOrderRepository;
import id.putra.core_erp.warehouse.module.inbound.model.repository.ReceivingRepository;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.ReceivingCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReceivingCommandAdapterImpl implements ReceivingCommandAdapter {

    @Inject
    private ReceivingRepository receivingRepository;

    @Inject
    private PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public ReceivingDto add(ReceivingDto dto) {
        return null;
    }

    @Override
    public ReceivingDto update(ReceivingDto dto) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return false;
    }

}
