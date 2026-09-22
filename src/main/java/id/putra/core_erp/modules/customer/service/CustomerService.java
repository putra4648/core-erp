package id.putra.core_erp.modules.customer.service;

import id.putra.core_erp.modules.customer.model.Customer;
import id.putra.core_erp.modules.customer.repository.CustomerRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CustomerService implements BaseService<Customer> {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void save(Customer customer) {
        customerRepository.persist(customer);
    }

    public Iterable<Customer> findAll(Customer c, Page page) {
        return customerRepository.findAll().list();
    }

    public void update(Customer customer) {
        customerRepository.getEntityManager().merge(customer);
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id);
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    public void add(Customer customer) {
        customerRepository.persist(customer);
    }
}
