package id.putra.core_erp.warehouse.module.customer.service.adapter.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.customer.dto.ContactPersonCustomerDto;

public interface ContactPersonCustomerQueryAdapter {
    Page<ContactPersonCustomerDto> getAll(Pageable pageable, String search);

    ContactPersonCustomerDto getById(Long id);
}

