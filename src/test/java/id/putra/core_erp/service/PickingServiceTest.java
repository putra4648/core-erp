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

import id.putra.core_erp.modules.outbound.model.PickingTask;
import id.putra.core_erp.modules.outbound.repository.PickingTaskRepository;
import id.putra.core_erp.modules.outbound.service.PickingService;
import id.putra.core_erp.support.ServiceDelegationAssertions;

/**
 * Plain JUnit + Mockito: {@link PickingService} carries no business rules, so
 * there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class PickingServiceTest {

	@Mock
	PickingTaskRepository pickingTaskRepository;

	@InjectMocks
	PickingService pickingService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		PickingTask entity = new PickingTask();

		pickingService.save(entity);

		verify(pickingTaskRepository).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		pickingService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(pickingTaskRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		PickingTask entity = new PickingTask();
		given(pickingTaskRepository.findById(3L)).willReturn(entity);

		assertSame(entity, pickingService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(pickingTaskRepository.count()).willReturn(42L);

		assertSame(42L, pickingService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(pickingTaskRepository);
	}
}
