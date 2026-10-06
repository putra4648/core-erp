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
<<<<<<< HEAD
	public void delete(Long id) {
		customerRepository.deleteById(id);
	}

	@Override
	public List<Customer> findAll(Customer c, Page page) {
=======
	public void save(Customer customer) {
		customerRepository.persist(customer);
	}

	@Override
	public Iterable<Customer> findAll(Customer c, Page page) {
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
		return customerRepository.findAll().list();
	}

	@Override
<<<<<<< HEAD
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
=======
	public void update(Customer customer) {
		customerRepository.getEntityManager().merge(customer);
	}

	@Override
	public Customer findById(Long id) {
		return customerRepository.findById(id);
	}

	@Override
	public void delete(Long id) {
		customerRepository.deleteById(id);
	}

>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
}
