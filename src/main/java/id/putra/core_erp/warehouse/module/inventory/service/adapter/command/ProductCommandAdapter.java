package id.putra.core_erp.warehouse.module.inventory.service.adapter.command;

import id.putra.core_erp.shared.dto.ProductDto;

public interface ProductCommandAdapter {

    void save(ProductDto dto);

    void update(ProductDto dto);

    void delete(Long id);

}
