package id.putra.core_erp.modules.product.service;

import java.util.List;

import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.modules.product.repository.ProductRepository;
import id.putra.core_erp.service.BaseService;
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

	@Override
	public void delete(Long id) {
		productRepository.deleteById(id);
	}

	@Override
	public List<Product> findAll(Product product, Page p) {
		return productRepository.findAll().list();
	}

	@Override
	public Product findById(Long id) {
		return productRepository.findById(id);
	}

	@Override
	public void save(Product dto) {
		productRepository.persist(dto);
	}

	@Override
	public void update(Product dto) {
		productRepository.getEntityManager().merge(dto);
	}
}
