package id.putra.core_erp.modules.budget.repository;

import java.util.Date;
import java.util.List;

import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.department.model.Department;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BudgetRepo implements PanacheRepository<Budget> {

	public List<Budget> findByDepartmentInAndFiscalYearAfter(Iterable<Department> department, Date fiscalYear) {
		return find("department IN ?1 AND fiscalYear > ?2", department, fiscalYear).list();
	}

	public List<Budget> findByDepartmentInAndFiscalYearBefore(Iterable<Department> department, Date fiscalYear) {
		return find("department IN ?1 AND fiscalYear < ?2", department, fiscalYear).list();
	}

}
