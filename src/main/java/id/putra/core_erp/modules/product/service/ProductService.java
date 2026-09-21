package id.putra.core_erp.modules.product.service;

import id.putra.core_erp.service.BaseService;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.modules.product.repository.ProductRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ProductService implements BaseService<Product> {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProduct(Product dto) {
        return productRepository.findById(dto.getId());
    }

    public Product save(Product dto) {
        productRepository.persist(dto);
        return dto;
    }

    public void update(Product dto) {
        productRepository.getEntityManager().merge(dto);
    }

    public Product findById(Long id) {
        return productRepository.findById(id);
    }

    public Iterable<Product> findAll() {
        return productRepository.findAll().list();
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public long countGetProducts(Product product, Page page) {
        String pattern = "%" + (product.getName() == null ? "" : product.getName().toLowerCase()) + "%";
        return productRepository.count(productFilter(product), pattern);
    }

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