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

import id.putra.core_erp.modules.parameter.model.ParameterLookup;
import id.putra.core_erp.modules.parameter.repository.ParameterLookupRepository;
import id.putra.core_erp.modules.parameter.service.ParameterLookupService;
import id.putra.core_erp.support.ServiceDelegationAssertions;

/**
 * Plain JUnit + Mockito: {@link ParameterLookupService} carries no business
 * rules, so there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class ParameterLookupServiceTest {

	@Mock
	ParameterLookupRepository parameterLookupRepository;

	@InjectMocks
	ParameterLookupService parameterLookupService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		ParameterLookup entity = new ParameterLookup();

		parameterLookupService.save(entity);

		verify(parameterLookupRepository).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		parameterLookupService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(parameterLookupRepository, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		ParameterLookup entity = new ParameterLookup();
		given(parameterLookupRepository.findById(3L)).willReturn(entity);

		assertSame(entity, parameterLookupService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(parameterLookupRepository.count()).willReturn(42L);

		assertSame(42L, parameterLookupService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(parameterLookupRepository);
	}
}
