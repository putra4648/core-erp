package id.putra.core_erp.warehouse.module.inbound.service.core;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.inbound.dto.ReceivingDto;

public interface ReceivingOrderService {
    ReceivingDto create(ReceivingDto dto);

    ReceivingDto update(ReceivingDto dto);

    Boolean delete(@NonNull Long id);

    ReceivingDto getById(@NonNull Long id, @NonNull Pageable pageable);

    Page<ReceivingDto> getAll(String search, @NonNull Pageable pageable);

    ReceivingDto patch(ReceivingDto body);
}

