package id.putra.core_erp.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.putra.core_erp.modules.customer.model.Customer;
import id.putra.core_erp.modules.customer.repository.CustomerRepository;
import id.putra.core_erp.modules.customer.service.CustomerService;
import id.putra.core_erp.support.ServiceDelegationAssertions;
import id.putra.core_erp.support.TestFixtures;

/**
 * Plain JUnit + Mockito: {@link CustomerService} carries no business rules, so
 * there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@Mock
	CustomerRepository customerRepository;

	@InjectMocks
	CustomerService customerService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Customer entity = TestFixtures.customer("CUST-1");

		customerService.save(entity);

		verify(customerRepository).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		customerService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(customerRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Customer entity = TestFixtures.customer("CUST-1");
		given(customerRepository.findById(3L)).willReturn(entity);

		assertSame(entity, customerService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(customerRepository.count()).willReturn(42L);

		assertSame(42L, customerService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(customerRepository);
	}
}
