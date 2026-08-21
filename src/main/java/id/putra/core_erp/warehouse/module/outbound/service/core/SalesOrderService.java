package id.putra.core_erp.warehouse.module.outbound.service.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.outbound.dto.SalesOrderDto;

public interface SalesOrderService {
    SalesOrderDto create(SalesOrderDto dto);

    SalesOrderDto update(SalesOrderDto dto);

    Boolean delete(Long id);

    SalesOrderDto getById(Long id);

    Page<SalesOrderDto> getAll(String search, Pageable pageable);
}

