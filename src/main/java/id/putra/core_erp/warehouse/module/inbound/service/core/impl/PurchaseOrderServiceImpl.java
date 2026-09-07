package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import org.jspecify.annotations.NonNull;

import id.putra.core_erp.warehouse.module.inbound.dto.PurchaseOrderDto;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.PurchaseOrderCommandAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.PurchaseOrderQueryAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.core.PurchaseOrderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Inject
    private PurchaseOrderCommandAdapter purchaseOrderCommandAdapter;

    @Inject
    private PurchaseOrderQueryAdapter purchaseOrderQueryAdapter;

    @Override
    public PurchaseOrderDto create(PurchaseOrderDto dto) {
        return purchaseOrderCommandAdapter.add(dto);
    }

    @Override
    public PurchaseOrderDto update(PurchaseOrderDto dto) {
        return purchaseOrderCommandAdapter.update(dto);
    }

    @Override
    public Boolean delete(@NonNull Long id) {
        return purchaseOrderCommandAdapter.delete(id);
    }

}
