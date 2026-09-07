package id.putra.core_erp.warehouse.module.inventory.service.core;

import id.putra.core_erp.shared.dto.ProductDto;

public interface ProductCoreService {

    ProductDto getProduct(ProductDto dto);

    void save(ProductDto dto);

    void update(ProductDto dto);

    void delete(Long id);

}
