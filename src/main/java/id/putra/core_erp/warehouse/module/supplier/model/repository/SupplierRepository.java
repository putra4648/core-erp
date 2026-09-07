package id.putra.core_erp.warehouse.module.supplier.model.repository;

import id.putra.core_erp.shared.entity.Supplier;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SupplierRepository implements PanacheRepository<Supplier> {
}
