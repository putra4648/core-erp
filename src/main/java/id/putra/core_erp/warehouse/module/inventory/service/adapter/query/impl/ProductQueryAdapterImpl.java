package id.putra.core_erp.warehouse.module.inventory.service.adapter.query.impl;

import id.putra.core_erp.shared.entity.Product;
import id.putra.core_erp.warehouse.module.inventory.model.repository.ProductRepository;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.query.ProductQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductQueryAdapterImpl implements ProductQueryAdapter {

    @Inject
    private ProductRepository productRepository;

    @Override
    public Product getProductById(Product dto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
