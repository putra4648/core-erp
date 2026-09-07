package id.putra.core_erp.warehouse.module.inbound.service.core;

import org.jspecify.annotations.NonNull;

import id.putra.core_erp.warehouse.module.inbound.dto.PurchaseOrderDto;

public interface PurchaseOrderService {
    PurchaseOrderDto create(PurchaseOrderDto dto);

    PurchaseOrderDto update(PurchaseOrderDto dto);

    Boolean delete(@NonNull Long id);

    // PurchaseOrderDto getById(@NonNull Long id, @NonNull Pageable pageable);

    //
    // pageable);

}
