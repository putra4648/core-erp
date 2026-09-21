package id.putra.core_erp.modules.supplier.repository;

import id.putra.core_erp.modules.supplier.model.ProductSupplier;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductSupplierRepository implements PanacheRepository<ProductSupplier> {
}
