package id.putra.core_erp.warehouse.module.inventory.service.adapter.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.shared.base.dto.ProductDto;

public interface ProductQueryAdapter {

    Page<ProductDto> getProducts(ProductDto dto, Pageable pageable);

    ProductDto getProductById(ProductDto dto);

}

