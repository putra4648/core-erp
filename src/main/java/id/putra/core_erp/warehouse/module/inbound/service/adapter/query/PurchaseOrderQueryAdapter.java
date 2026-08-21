package id.putra.core_erp.warehouse.module.inbound.service.adapter.query;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.inbound.dto.PurchaseOrderDto;

public interface PurchaseOrderQueryAdapter {
    PurchaseOrderDto getById(@NonNull Long id, @NonNull Pageable pageable);

    Page<PurchaseOrderDto> getAll(PurchaseOrderDto dto, @NonNull Pageable pageable);
}

