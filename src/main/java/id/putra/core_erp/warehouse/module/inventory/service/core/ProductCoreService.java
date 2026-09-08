package id.putra.core_erp.warehouse.module.inventory.service.core;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.shared.entity.Product;
import io.quarkus.panache.common.Page;

public interface ProductCoreService {

    Product getProduct(Product dto);

    void save(Product dto);

    void update(Product dto);

    void delete(Long id);

    long countGetProducts(Product product, Page ofSize);

    DataProvider<Product, Void> getProducts(Product searchParam, Page ofSize);

}
