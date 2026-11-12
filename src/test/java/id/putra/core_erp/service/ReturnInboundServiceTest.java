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

import id.putra.core_erp.modules.inbound.model.Return;
import id.putra.core_erp.modules.inbound.repository.ReturnRepository;
import id.putra.core_erp.modules.inbound.service.ReturnInboundService;
import id.putra.core_erp.support.ServiceDelegationAssertions;

/**
 * Plain JUnit + Mockito: {@link ReturnInboundService} carries no business
 * rules, so there
 * is no CDI, transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class ReturnInboundServiceTest {

	@Mock
	ReturnRepository returnRepository;

	@InjectMocks
	ReturnInboundService returnInboundService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Return entity = new Return();

		returnInboundService.save(entity);

		verify(returnRepository).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		returnInboundService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(returnRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Return entity = new Return();
		given(returnRepository.findById(3L)).willReturn(entity);

		assertSame(entity, returnInboundService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(returnRepository.count()).willReturn(42L);

		assertSame(42L, returnInboundService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(returnRepository);
	}
}
