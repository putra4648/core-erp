package id.putra.core_erp.warehouse.module.customer.service.core;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.shared.entity.Customer;
import id.putra.core_erp.warehouse.module.customer.model.repository.CustomerRepository;
import io.quarkus.panache.common.Page;
import jakarta.transaction.Transactional;

@Transactional
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer getById(Long id) {
        return null;
    }

    public List<Customer> create(List<Customer> dtos) {
        return null;
    }

    public Customer update(Long id, Customer dto) {
        return null;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public DataProvider<Customer, Void> getAll(String value, Page ofSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }
}
