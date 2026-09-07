package id.putra.core_erp.warehouse.module.inventory.service.core.impl;

import id.putra.core_erp.shared.dto.ProductDto;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.command.ProductCommandAdapter;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.query.ProductQueryAdapter;
import id.putra.core_erp.warehouse.module.inventory.service.core.ProductCoreService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductServiceImpl implements ProductCoreService {
    @Inject
    private ProductQueryAdapter productQueryAdapter;

    @Inject
    private ProductCommandAdapter productCommandAdapter;

    @Override
    public ProductDto getProduct(ProductDto dto) {
        return productQueryAdapter.getProductById(dto);
    }

    @Override
    public void save(ProductDto dto) {
        productCommandAdapterpersist(dto);
    }

    @Override
    public void update(ProductDto dto) {
        productCommandAdapter.update(dto);
    }

    @Override
    public void delete(Long id) {
        productCommandAdapter.delete(id);
    }

}
