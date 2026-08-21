package id.putra.core_erp.warehouse.module.customer.service.adapter.command;

import id.putra.core_erp.warehouse.module.customer.dto.ContactPersonCustomerDto;

public interface ContactPersonCustomerCommandAdapter {
    ContactPersonCustomerDto create(ContactPersonCustomerDto dto);

    ContactPersonCustomerDto update(Long id, ContactPersonCustomerDto dto);

    void delete(Long id);
}

