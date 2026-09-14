package id.putra.core_erp.warehouse.module.customer.service;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.shared.entity.Customer;
import io.quarkus.panache.common.Page;

public interface CustomerService {

    void add(Customer customer);

    void update(Long id, Customer customer);

    void delete(Long id);

    Customer getById(Long id);

    List<Customer> create(List<Customer> dtos);

    DataProvider<Customer, Void> getAll(String value, Page page);
}
