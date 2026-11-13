package id.putra.core_erp.modules.budget.repository;

import java.util.List;

import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Fiscal-year range queries.
 *
 * <p>{@code Budget.fiscalYear} is an {@code Integer} column. The parameter is typed
 * {@code Integer} to match: binding a {@code java.util.Date} here compares a calendar year
 * against a date, which PostgreSQL rejects at execution time. Both bounds are exclusive, so
 * a budget in exactly {@code fiscalYear} falls outside both ranges and the two queries never
 * overlap.
 */
@ApplicationScoped
public class BudgetRepo implements SoftDeleteRepository<Budget> {

	/** Budgets for the given departments with a fiscal year strictly greater than {@code fiscalYear}. */
	public List<Budget> findByDepartmentInAndFiscalYearAfter(Iterable<Department> department, Integer fiscalYear) {
		return find("department IN ?1 AND fiscalYear > ?2", department, fiscalYear).list();
	}

	/** Budgets for the given departments with a fiscal year strictly less than {@code fiscalYear}. */
	public List<Budget> findByDepartmentInAndFiscalYearBefore(Iterable<Department> department, Integer fiscalYear) {
		return find("department IN ?1 AND fiscalYear < ?2", department, fiscalYear).list();
	}

}
