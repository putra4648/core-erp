package id.putra.core_erp.modules.product.repository;

import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements SoftDeleteRepository<Product> {

}
