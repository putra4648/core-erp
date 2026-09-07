package id.putra.core_erp.warehouse.module.customer.service.core;

import java.util.List;

import id.putra.core_erp.warehouse.module.customer.dto.CustomerDto;
import id.putra.core_erp.warehouse.module.customer.model.repository.CustomerRepository;
import jakarta.transaction.Transactional;

@Transactional
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public CustomerDto getById(Long id) {
        return null;
    }

    public List<CustomerDto> create(List<CustomerDto> dtos) {
        return null;
    }

    public CustomerDto update(Long id, CustomerDto dto) {
        return null;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
