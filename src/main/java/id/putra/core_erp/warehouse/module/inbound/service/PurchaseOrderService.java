package id.putra.core_erp.warehouse.module.inbound.service;

import java.util.List;

import org.jspecify.annotations.NonNull;

import id.putra.core_erp.warehouse.module.inbound.model.entity.PurchaseOrder;
import io.quarkus.panache.common.Page;

public interface PurchaseOrderService {
    PurchaseOrder create(PurchaseOrder dto);

    PurchaseOrder update(PurchaseOrder dto);

    Boolean delete(@NonNull Long id);

    List<PurchaseOrder> getAll(PurchaseOrder dto, Page page);

}
