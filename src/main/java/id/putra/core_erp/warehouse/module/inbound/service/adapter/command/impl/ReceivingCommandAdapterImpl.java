package id.putra.core_erp.warehouse.module.inbound.service.adapter.command.impl;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Receiving;
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
    public Receiving add(Receiving dto) {
        return null;
    }

    @Override
    public Receiving update(Receiving dto) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return false;
    }

}
