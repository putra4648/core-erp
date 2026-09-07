package id.putra.core_erp.warehouse.module.customer.service.adapter.query;

import id.putra.core_erp.warehouse.module.customer.dto.CustomerDto;

public interface CustomerQueryAdapter {

    CustomerDto getById(Long id);
}
