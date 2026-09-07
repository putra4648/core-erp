package id.putra.core_erp.warehouse.module.inventory.model.repository;

import id.putra.core_erp.shared.entity.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

}
