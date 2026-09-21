package id.putra.core_erp.modules.customer.service.impl;

import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import id.putra.core_erp.modules.customer.model.Customer;
import id.putra.core_erp.modules.customer.repository.CustomerRepository;
import id.putra.core_erp.modules.customer.service.CustomerService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
    }

    @Override
    public List<Customer> create(List<Customer> dtos) {
        dtos.forEach(this::linkContactPerson);
        customerRepository.persist(dtos);
        return dtos;
    }

    @Override
    public void update(Long id, Customer dto) {
        Customer customer = getById(id);
        customer.setCode(dto.getCode());
        customer.setName(dto.getName());
        customer.setIsActive(dto.getIsActive());
        customer.setContactPerson(dto.getContactPerson());
        linkContactPerson(customer);
    }

    @Override
    public void delete(Long id) {
        customerRepository.delete(getById(id));
    }

    @Override
    public DataProvider<Customer, Void> getAll(String value, Page page) {
        String search = value == null ? "" : value.trim().toLowerCase();
        String pattern = "%" + search + "%";

        return DataProvider.fromCallbacks(
                query -> findCustomers(search, pattern, query),
                query -> countCustomers(search, pattern));
    }

    private Stream<Customer> findCustomers(String search, String pattern, Query<Customer, Void> query) {
        if (search.isEmpty()) {
            return customerRepository.findAll()
                    .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                    .list().stream();
        }
        return customerRepository.find("lower(code) like ?1 or lower(name) like ?1", pattern)
                .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                .list().stream();
    }

    private int countCustomers(String search, String pattern) {
        if (search.isEmpty()) {
            return Math.toIntExact(customerRepository.count());
        }
        return Math.toIntExact(customerRepository.count("lower(code) like ?1 or lower(name) like ?1", pattern));
    }

    private void linkContactPerson(Customer customer) {
        if (customer.getContactPerson() != null) {
            customer.getContactPerson().setCustomer(customer);
        }
    }

    @Override
    public void add(Customer customer) {
        customerRepository.persist(customer);
    }
}
