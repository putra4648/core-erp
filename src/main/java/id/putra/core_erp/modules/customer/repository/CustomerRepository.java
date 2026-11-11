package id.putra.core_erp.modules.customer.repository;

import id.putra.core_erp.modules.customer.model.Customer;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepository implements SoftDeleteRepository<Customer> {
}