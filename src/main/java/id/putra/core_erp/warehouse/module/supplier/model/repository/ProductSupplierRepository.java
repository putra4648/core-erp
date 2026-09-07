package id.putra.core_erp.warehouse.module.supplier.model.repository;

import id.putra.core_erp.warehouse.module.supplier.model.entity.ProductSupplier;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductSupplierRepository implements PanacheRepository<ProductSupplier> {
}
