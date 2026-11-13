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

import id.putra.core_erp.modules.warehouse.model.Rack;
import id.putra.core_erp.modules.warehouse.repository.RackRepository;
import id.putra.core_erp.modules.warehouse.service.RackCoreService;
import id.putra.core_erp.support.ServiceDelegationAssertions;
import id.putra.core_erp.support.TestFixtures;

/**
 * Plain JUnit + Mockito: {@link RackCoreService} carries no business rules, so
 * there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class RackCoreServiceTest {

	@Mock
	RackRepository rackRepository;

	@InjectMocks
	RackCoreService rackCoreService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Rack entity = TestFixtures.rack("R-1", null);

		rackCoreService.save(entity);

		verify(rackRepository).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		rackCoreService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(rackRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Rack entity = TestFixtures.rack("R-1", null);
		given(rackRepository.findById(3L)).willReturn(entity);

		assertSame(entity, rackCoreService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(rackRepository.count()).willReturn(42L);

		assertSame(42L, rackCoreService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(rackRepository);
	}
}
