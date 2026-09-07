package id.putra.core_erp.warehouse.module.supplier.model.repository;

import id.putra.core_erp.warehouse.module.supplier.model.entity.ContactPersonSupplier;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContactPersonSupplierRepository implements PanacheRepository<ContactPersonSupplier> {
}
