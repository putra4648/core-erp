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

import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.modules.supplier.repository.SupplierRepository;
import id.putra.core_erp.modules.supplier.service.SupplierService;
import id.putra.core_erp.support.ServiceDelegationAssertions;

/**
 * Plain JUnit + Mockito: {@link SupplierService} carries no business rules, so
 * there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

	@Mock
	SupplierRepository supplierRepository;

	@InjectMocks
	SupplierService supplierService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Supplier entity = new Supplier();

		supplierService.save(entity);

		verify(supplierRepository).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		supplierService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(supplierRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Supplier entity = new Supplier();
		given(supplierRepository.findById(3L)).willReturn(entity);

		assertSame(entity, supplierService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(supplierRepository.count()).willReturn(42L);

		assertSame(42L, supplierService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(supplierRepository);
	}
}
