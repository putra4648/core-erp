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

import id.putra.core_erp.modules.inbound.model.Labelling;
import id.putra.core_erp.modules.inbound.repository.LabellingRepository;
import id.putra.core_erp.modules.inbound.service.LabellingService;
import id.putra.core_erp.support.ServiceDelegationAssertions;

/**
 * Plain JUnit + Mockito: {@link LabellingService} carries no business rules, so
 * there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class LabellingServiceTest {

	@Mock
	LabellingRepository labellingRepository;

	@InjectMocks
	LabellingService labellingService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Labelling entity = new Labelling();

		labellingService.save(entity);

		verify(labellingRepository).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		labellingService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(labellingRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Labelling entity = new Labelling();
		given(labellingRepository.findById(3L)).willReturn(entity);

		assertSame(entity, labellingService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(labellingRepository.count()).willReturn(42L);

		assertSame(42L, labellingService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(labellingRepository);
	}
}
