package id.putra.core_erp.modules.supplier.repository;

import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SupplierRepository implements SoftDeleteRepository<Supplier> {
}
