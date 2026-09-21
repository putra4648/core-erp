package id.putra.core_erp.modules.supplier.repository;

import id.putra.core_erp.modules.supplier.model.ContactPersonSupplier;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContactPersonSupplierRepository implements PanacheRepository<ContactPersonSupplier> {
}
