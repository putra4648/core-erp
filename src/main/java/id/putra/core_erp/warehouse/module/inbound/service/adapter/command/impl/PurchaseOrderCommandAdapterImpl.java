package id.putra.core_erp.warehouse.module.inbound.service.adapter.command.impl;

import id.putra.core_erp.warehouse.module.inbound.dto.PurchaseOrderDto;
import id.putra.core_erp.warehouse.module.inbound.model.repository.PurchaseOrderRepository;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.PurchaseOrderCommandAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PurchaseOrderCommandAdapterImpl implements PurchaseOrderCommandAdapter {

    @Inject
    private PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public PurchaseOrderDto add(PurchaseOrderDto dto) {
        return null;
    }

    @Override
    public PurchaseOrderDto update(PurchaseOrderDto dto) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {

        return false;
    }

}
