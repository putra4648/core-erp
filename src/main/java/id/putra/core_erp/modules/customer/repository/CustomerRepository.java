package id.putra.core_erp.modules.customer.repository;

import id.putra.core_erp.modules.customer.model.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {
}