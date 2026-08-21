package id.putra.core_erp.warehouse.module.inventory.service.adapter.command;

import id.putra.core_erp.warehouse.shared.base.dto.ProductDto;

public interface ProductCommandAdapter {

    ProductDto save(ProductDto dto);

    ProductDto update(ProductDto dto);

    Boolean delete(Long id);

}

