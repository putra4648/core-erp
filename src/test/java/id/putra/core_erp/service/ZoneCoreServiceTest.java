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

import id.putra.core_erp.modules.warehouse.model.Zone;
import id.putra.core_erp.modules.warehouse.repository.ZoneRepository;
import id.putra.core_erp.modules.warehouse.service.ZoneCoreService;
import id.putra.core_erp.support.ServiceDelegationAssertions;
import id.putra.core_erp.support.TestFixtures;

/**
 * Plain JUnit + Mockito: {@link ZoneCoreService} carries no business rules, so
 * there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class ZoneCoreServiceTest {

	@Mock
	ZoneRepository zoneRepository;

	@InjectMocks
	ZoneCoreService zoneCoreService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Zone entity = TestFixtures.zone("Z-1", null);

		zoneCoreService.save(entity);

		verify(zoneRepository).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		zoneCoreService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(zoneRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Zone entity = TestFixtures.zone("Z-1", null);
		given(zoneRepository.findById(3L)).willReturn(entity);

		assertSame(entity, zoneCoreService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(zoneRepository.count()).willReturn(42L);

		assertSame(42L, zoneCoreService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(zoneRepository);
	}
}
