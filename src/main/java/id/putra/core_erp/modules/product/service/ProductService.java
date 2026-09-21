package id.putra.core_erp.modules.product.service;

import io.quarkus.panache.common.Page;
import com.vaadin.flow.data.provider.DataProvider;
import id.putra.core_erp.modules.product.model.Product;

public interface ProductService {

    Product getProduct(Product dto);

    void save(Product dto);

    void update(Product dto);

    void delete(Long id);

    long countGetProducts(Product product, Page ofSize);

    DataProvider<Product, Void> getProducts(Product searchParam, Page ofSize);

}
