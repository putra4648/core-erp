package id.putra.core_erp.warehouse.module.inbound.service.adapter.command;

import id.putra.core_erp.warehouse.module.inbound.dto.PurchaseOrderDto;

public interface PurchaseOrderCommandAdapter {
    PurchaseOrderDto add(PurchaseOrderDto dto);

    PurchaseOrderDto update(PurchaseOrderDto dto);

    Boolean delete(Long id);

    PurchaseOrderDto patch(PurchaseOrderDto dto);
}

