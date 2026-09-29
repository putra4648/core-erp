package id.putra.core_erp.modules.customer.service;

import java.util.List;

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

	@Override
	public void delete(Long id) {
		customerRepository.deleteById(id);
	}

	@Override
	public List<Customer> findAll(Customer c, Page page) {
		return customerRepository.findAll().list();
	}

	@Override
	public Customer findById(Long id) {
		return customerRepository.findById(id);
	}

	@Override
	public void save(Customer customer) {
		customerRepository.persist(customer);
	}

	@Override
	public void update(Customer customer) {
		customerRepository.update("code = ?1, name = ?2 WHERE id = ?3", customer.getCode(), customer.getName(),
				customer.getId());
	}
}
