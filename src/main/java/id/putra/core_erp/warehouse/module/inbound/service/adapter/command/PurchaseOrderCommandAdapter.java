package id.putra.core_erp.warehouse.module.inbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.inbound.model.entity.PurchaseOrder;

public interface PurchaseOrderCommandAdapter {
    PurchaseOrder add(PurchaseOrder dto);

    PurchaseOrder update(PurchaseOrder dto);

    Boolean delete(Long id);

}
