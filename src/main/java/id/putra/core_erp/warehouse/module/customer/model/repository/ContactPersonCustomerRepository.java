package id.putra.core_erp.warehouse.module.customer.model.repository;

import id.putra.core_erp.shared.entity.ContactPersonCustomer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContactPersonCustomerRepository implements PanacheRepository<ContactPersonCustomer> {
}
