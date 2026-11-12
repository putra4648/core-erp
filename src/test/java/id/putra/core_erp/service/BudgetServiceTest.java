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

import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.budget.repository.BudgetRepo;
import id.putra.core_erp.modules.budget.service.BudgetService;
import id.putra.core_erp.support.ServiceDelegationAssertions;
import id.putra.core_erp.support.TestFixtures;

/**
 * Plain JUnit + Mockito: {@link BudgetService} carries no business rules, so
 * there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

	@Mock
	BudgetRepo budgetRepo;

	@InjectMocks
	BudgetService budgetService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Budget entity = TestFixtures.budget(null, "B-1");

		budgetService.save(entity);

		verify(budgetRepo).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		budgetService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(budgetRepo, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Budget entity = TestFixtures.budget(null, "B-1");
		given(budgetRepo.findById(3L)).willReturn(entity);

		assertSame(entity, budgetService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(budgetRepo.count()).willReturn(42L);

		assertSame(42L, budgetService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(budgetRepo);
	}
}
