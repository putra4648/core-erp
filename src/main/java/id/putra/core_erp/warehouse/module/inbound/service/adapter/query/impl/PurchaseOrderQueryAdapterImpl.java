package id.putra.core_erp.warehouse.module.inbound.service.adapter.query.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.PurchaseOrder;
import id.putra.core_erp.warehouse.module.inbound.model.repository.PurchaseOrderRepository;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.PurchaseOrderQueryAdapter;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PurchaseOrderQueryAdapterImpl implements PurchaseOrderQueryAdapter {
    @Inject
    PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public List<PurchaseOrder> list(PurchaseOrder dto, Page page) {
        return purchaseOrderRepository.find("poNumber LIKE ?1", "%" + dto.getPoNumber() + "%").page(page).list();
    }

}
