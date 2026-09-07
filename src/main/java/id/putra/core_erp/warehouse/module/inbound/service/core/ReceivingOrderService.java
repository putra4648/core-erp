package id.putra.core_erp.warehouse.module.inbound.service.core;

import org.jspecify.annotations.NonNull;

import id.putra.core_erp.warehouse.module.inbound.dto.ReceivingDto;

public interface ReceivingOrderService {
    ReceivingDto create(ReceivingDto dto);

    ReceivingDto update(ReceivingDto dto);

    Boolean delete(@NonNull Long id);

    // ReceivingDto getById(@NonNull Long id, @NonNull Pageable pageable);

    //

}
