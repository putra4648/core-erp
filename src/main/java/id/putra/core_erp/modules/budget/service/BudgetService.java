package id.putra.core_erp.modules.budget.service;

import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.budget.repository.BudgetDetailRepo;
import id.putra.core_erp.modules.budget.repository.BudgetRepo;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class BudgetService implements BaseService<Budget> {

	private final BudgetRepo budgetRepo;
	private final DepartmentRepo departmentRepo;
	private final BudgetDetailRepo budgetDetailRepo;

	public BudgetService(BudgetRepo budgetRepo, DepartmentRepo departmentRepo, BudgetDetailRepo budgetDetailRepo) {
		this.budgetRepo = budgetRepo;
		this.departmentRepo = departmentRepo;
		this.budgetDetailRepo = budgetDetailRepo;
	}

	@Override
	public void save(Budget entity) {
		budgetRepo.persist(entity);
	}

	@Override
	public Budget findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Budget entity) {
		budgetRepo.getEntityManager().merge(entity);
	}

	@Override
	public Iterable<Budget> findAll(Budget entity, Page page) {
		return budgetRepo.findAll().page(page).list();
	}

}
