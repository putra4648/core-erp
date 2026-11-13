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

import id.putra.core_erp.modules.outbound.model.Carrier;
import id.putra.core_erp.modules.outbound.repository.CarrierRepository;
import id.putra.core_erp.modules.outbound.service.CarrierService;
import id.putra.core_erp.support.ServiceDelegationAssertions;

/**
 * Plain JUnit + Mockito: {@link CarrierService} carries no business rules, so
 * there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class CarrierServiceTest {

	@Mock
	CarrierRepository carrierRepository;

	@InjectMocks
	CarrierService carrierService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Carrier entity = new Carrier();

		carrierService.save(entity);

		verify(carrierRepository).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		carrierService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(carrierRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Carrier entity = new Carrier();
		given(carrierRepository.findById(3L)).willReturn(entity);

		assertSame(entity, carrierService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(carrierRepository.count()).willReturn(42L);

		assertSame(42L, carrierService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(carrierRepository);
	}
}
