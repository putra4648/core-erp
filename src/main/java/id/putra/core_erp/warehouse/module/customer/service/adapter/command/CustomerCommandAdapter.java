package id.putra.core_erp.warehouse.module.customer.service.adapter.command;

import id.putra.core_erp.warehouse.module.customer.dto.CustomerDto;

public interface CustomerCommandAdapter {
    CustomerDto create(CustomerDto dto);

    CustomerDto update(Long id, CustomerDto dto);

    void delete(Long id);
}

