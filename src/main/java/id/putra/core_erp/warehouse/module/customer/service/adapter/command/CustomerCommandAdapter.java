package id.putra.core_erp.warehouse.module.customer.service.adapter.command;

import id.putra.core_erp.shared.entity.Customer;

public interface CustomerCommandAdapter {
    Customer create(Customer dto);

    Customer update(Long id, Customer dto);

    void delete(Long id);
}
