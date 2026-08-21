package id.putra.core_erp.warehouse.module.inbound.service.adapter.query;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.inbound.dto.ReceivingDto;

public interface ReceivingQueryAdapter {
    ReceivingDto getById(@NonNull Long id, @NonNull Pageable pageable);

    Page<ReceivingDto> getAll(String search, @NonNull Pageable pageable);
}

