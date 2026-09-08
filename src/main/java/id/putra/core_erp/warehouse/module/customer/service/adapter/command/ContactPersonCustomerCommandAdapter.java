package id.putra.core_erp.warehouse.module.customer.service.adapter.command;

import id.putra.core_erp.shared.entity.ContactPersonCustomer;

public interface ContactPersonCustomerCommandAdapter {
    ContactPersonCustomer create(ContactPersonCustomer dto);

    ContactPersonCustomer update(Long id, ContactPersonCustomer dto);

    void delete(Long id);
}
