package id.putra.core_erp.warehouse.module.customer.service.adapter.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.customer.dto.CustomerDto;

public interface CustomerQueryAdapter {
    Page<CustomerDto> getAll(Pageable pageable, String search);

    CustomerDto getById(Long id);
}

