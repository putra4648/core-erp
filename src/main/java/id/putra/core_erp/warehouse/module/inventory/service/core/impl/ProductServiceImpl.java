package id.putra.core_erp.warehouse.module.inventory.service.core.impl;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.shared.entity.Product;
import id.putra.core_erp.warehouse.module.inventory.model.repository.ProductRepository;
import id.putra.core_erp.warehouse.module.inventory.service.core.ProductCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ProductServiceImpl implements ProductCoreService {
    @Inject
    private ProductRepository productRepository;

    @Override
    public Product getProduct(Product dto) {
        return productRepository.findById(dto.getId());
    }

    @Override
    public void save(Product dto) {
        productRepository.persist(dto);
    }

    @Override
    public void update(Product dto) {
        productRepository.getEntityManager().merge(dto);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public long countGetProducts(Product product, Page page) {
        String pattern = "%" + (product.getName() == null ? "" : product.getName().toLowerCase()) + "%";
        return productRepository.count(productFilter(product), pattern);
    }

    @Override
    public DataProvider<Product, Void> getProducts(Product searchParam, Page page) {
        String filter = productFilter(searchParam);
        String pattern = "%" + (searchParam.getName() == null ? "" : searchParam.getName().toLowerCase()) + "%";
        return DataProvider.fromCallbacks(
                query -> productRepository.find(filter, pattern)
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math.toIntExact(productRepository.count(filter, pattern)));
    }

    private String productFilter(Product product) {
        return "lower(name) like ?1 or lower(sku) like ?1";
    }

}
