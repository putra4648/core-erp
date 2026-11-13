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

import id.putra.core_erp.modules.outbound.model.Packing;
import id.putra.core_erp.modules.outbound.repository.PackingRepository;
import id.putra.core_erp.modules.outbound.service.PackingService;
import id.putra.core_erp.support.ServiceDelegationAssertions;

/**
 * Plain JUnit + Mockito: {@link PackingService} carries no business rules, so
 * there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class PackingServiceTest {

	@Mock
	PackingRepository packingRepository;

	@InjectMocks
	PackingService packingService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Packing entity = new Packing();

		packingService.save(entity);

		verify(packingRepository).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		packingService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(packingRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Packing entity = new Packing();
		given(packingRepository.findById(3L)).willReturn(entity);

		assertSame(entity, packingService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(packingRepository.count()).willReturn(42L);

		assertSame(42L, packingService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(packingRepository);
	}
}
