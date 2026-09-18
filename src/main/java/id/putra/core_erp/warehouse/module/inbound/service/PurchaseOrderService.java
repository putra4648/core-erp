package id.putra.core_erp.warehouse.module.inbound.service;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.PurchaseOrder;
import io.quarkus.panache.common.Page;

public interface PurchaseOrderService {
    PurchaseOrder create(PurchaseOrder dto);

    PurchaseOrder update(PurchaseOrder dto);

    Boolean delete( Long id);

    List<PurchaseOrder> getAll(PurchaseOrder dto, Page page);

}
