package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import java.util.List;

import org.jspecify.annotations.NonNull;

import id.putra.core_erp.warehouse.module.inbound.model.entity.PurchaseOrder;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.command.PurchaseOrderCommandAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.PurchaseOrderQueryAdapter;
import id.putra.core_erp.warehouse.module.inbound.service.core.PurchaseOrderService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Inject
    private PurchaseOrderCommandAdapter purchaseOrderCommandAdapter;

    @Inject
    private PurchaseOrderQueryAdapter purchaseOrderQueryAdapter;

    @Override
    public PurchaseOrder create(PurchaseOrder dto) {
        return purchaseOrderCommandAdapter.add(dto);
    }

    @Override
    public PurchaseOrder update(PurchaseOrder dto) {
        return purchaseOrderCommandAdapter.update(dto);
    }

    @Override
    public Boolean delete(@NonNull Long id) {
        return purchaseOrderCommandAdapter.delete(id);
    }

    @Override
    public List<PurchaseOrder> getAll(PurchaseOrder dto, Page page) {
        return purchaseOrderQueryAdapter.list(dto, page);
    }

}
