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
	public void save(Customer customer) {
		customerRepository.persist(customer);
	}

	@Override
	public List<Customer> findAll(Customer c, Page page) {
		return customerRepository.findAll().list();
	}

	@Override
	public void update(Customer customer) {
		customerRepository.getEntityManager().merge(customer);
	}

	@Override
	public Customer findById(Long id) {
		return customerRepository.findById(id);
	}

	@Override
	public void delete(Long id) {
		customerRepository.softDeleteById(id);
	}

	@Override
	public long count() {
		return customerRepository.count();
	}
}
