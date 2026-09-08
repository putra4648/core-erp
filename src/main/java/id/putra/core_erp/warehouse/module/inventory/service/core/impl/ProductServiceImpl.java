package id.putra.core_erp.warehouse.module.inventory.service.core.impl;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.shared.entity.Product;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.command.ProductCommandAdapter;
import id.putra.core_erp.warehouse.module.inventory.service.adapter.query.ProductQueryAdapter;
import id.putra.core_erp.warehouse.module.inventory.service.core.ProductCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductServiceImpl implements ProductCoreService {
    @Inject
    private ProductQueryAdapter productQueryAdapter;

    @Inject
    private ProductCommandAdapter productCommandAdapter;

    @Override
    public Product getProduct(Product dto) {
        return productQueryAdapter.getProductById(dto);
    }

    @Override
    public void save(Product dto) {
        productCommandAdapter.save(dto);
    }

    @Override
    public void update(Product dto) {
        productCommandAdapter.update(dto);
    }

    @Override
    public void delete(Long id) {
        productCommandAdapter.delete(id);
    }

    @Override
    public long countGetProducts(Product product, Page ofSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countGetProducts'");
    }

    @Override
    public DataProvider<Product, Void> getProducts(Product searchParam, Page ofSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProducts'");
    }

}
