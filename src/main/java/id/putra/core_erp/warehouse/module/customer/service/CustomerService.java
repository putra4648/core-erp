package id.putra.core_erp.warehouse.module.customer.service;

import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import id.putra.core_erp.shared.entity.Customer;
import id.putra.core_erp.warehouse.module.customer.model.repository.CustomerRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CustomerService {
    @Inject
    private CustomerRepository customerRepository;

    public Customer getById(Long id) {
        return customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
    }

    public List<Customer> create(List<Customer> dtos) {
        dtos.forEach(this::linkContactPerson);
        customerRepository.persist(dtos);
        return dtos;
    }

    public Customer update(Long id, Customer dto) {
        Customer customer = getById(id);
        customer.setCode(dto.getCode());
        customer.setName(dto.getName());
        customer.setIsActive(dto.getIsActive());
        customer.setContactPerson(dto.getContactPerson());
        linkContactPerson(customer);
        return customer;
    }

    public void delete(Long id) {
        customerRepository.delete(getById(id));
    }

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
}
