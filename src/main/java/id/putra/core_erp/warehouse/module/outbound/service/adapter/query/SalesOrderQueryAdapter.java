package id.putra.core_erp.warehouse.module.outbound.service.adapter.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.outbound.dto.SalesOrderDto;

public interface SalesOrderQueryAdapter {
    SalesOrderDto getById(Long id);

    Page<SalesOrderDto> getAll(String search, Pageable pageable);
}

