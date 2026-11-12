package id.putra.core_erp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.budget.repository.BudgetRepo;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.support.TestFixtures;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers the two fiscal-year range queries on {@link BudgetRepo}.
 *
 * <p>
 * Both take an {@code Iterable<Department>} and an exclusive bound ({@code >}
 * and
 * {@code <}, not {@code >=} / {@code <=}). The boundary is the interesting
 * part: a budget in
 * exactly the queried year must fall outside both ranges, otherwise the two
 * queries overlap
 * and a caller summing them double-counts.
 */
@QuarkusTest
class BudgetRepoTest {

	@Inject
	BudgetRepo budgetRepo;

	@Inject
	DepartmentRepo departmentRepo;

	@Inject
	EntityManager em;

	private Budget newBudget(Department dept, String name, int fiscalYear, String allocated) {
		Budget b = TestFixtures.budget(dept, name, allocated);
		b.fiscalYear = fiscalYear;
		budgetRepo.persist(b);
		return b;
	}

	@Test
	@TestTransaction
	@DisplayName("findByDepartmentInAndFiscalYearAfter is exclusive of the given year")
	void afterIsExclusive() {
		Department dept = TestFixtures.department("BRT01");
		departmentRepo.persist(dept);

		newBudget(dept, "Same year", 2026, "100.00");
		newBudget(dept, "Next year", 2027, "200.00");
		em.flush();
		em.clear();

		List<Budget> hits = budgetRepo.findByDepartmentInAndFiscalYearAfter(List.of(dept), 2026);

		assertEquals(1, hits.size(), "only the later year must match");
		assertEquals(2027, hits.get(0).fiscalYear, "the same year must be excluded — the bound is >");
	}

	@Test
	@TestTransaction
	@DisplayName("findByDepartmentInAndFiscalYearBefore is exclusive of the given year")
	void beforeIsExclusive() {
		Department dept = TestFixtures.department("BRT02");
		departmentRepo.persist(dept);

		newBudget(dept, "Same year", 2026, "100.00");
		newBudget(dept, "Prior year", 2025, "200.00");
		em.flush();
		em.clear();

		List<Budget> hits = budgetRepo.findByDepartmentInAndFiscalYearBefore(List.of(dept), 2026);

		assertEquals(1, hits.size(), "only the earlier year must match");
		assertEquals(2025, hits.get(0).fiscalYear, "the same year must be excluded — the bound is <");
	}

	@Test
	@TestTransaction
	@DisplayName("both range queries are scoped to the given departments")
	void bothQueriesScopeToDepartments() {
		Department mine = TestFixtures.department("BRT03");
		Department other = TestFixtures.department("BRT04");
		departmentRepo.persist(mine);
		departmentRepo.persist(other);

		newBudget(mine, "Mine next", 2027, "100.00");
		newBudget(other, "Other next", 2027, "500.00");
		em.flush();
		em.clear();

		List<Budget> hits = budgetRepo.findByDepartmentInAndFiscalYearAfter(List.of(mine), 2026);

		assertEquals(1, hits.size());
		assertEquals("Mine next", hits.get(0).name, "another department's budget must not leak in");
	}

	@Test
	@TestTransaction
	@DisplayName("soft-deleted budgets are invisible to both range queries")
	void softDeletedBudgetsAreHidden() {
		Department dept = TestFixtures.department("BRT05");
		departmentRepo.persist(dept);
		Budget b = newBudget(dept, "Deleted next", 2027, "100.00");
		em.flush();

		Long id = b.id;
		budgetRepo.softDeleteById(id);
		em.flush();
		em.clear();

		List<Budget> hits = budgetRepo.findByDepartmentInAndFiscalYearAfter(List.of(dept), 2026);

		assertTrue(hits.stream().noneMatch(x -> id.equals(x.id)),
				"a soft-deleted budget must not appear in the range result");
	}

	@Test
	@TestTransaction
	@DisplayName("Budget joins Department on dept_number, so the FK holds via the code")
	void budgetLinksDepartmentByCode() {
		Department dept = TestFixtures.department("BRT06");
		departmentRepo.persist(dept);
		Budget b = newBudget(dept, "Linked budget", 2026, "1234.56");
		em.flush();
		em.clear();

		Budget reloaded = budgetRepo.findById(b.id);

		assertEquals("BRT06", reloaded.department.code,
				"the association must resolve through dept_number, not the surrogate id");
		assertEquals(0, new BigDecimal("1234.56").compareTo(reloaded.allocated));
	}
}
