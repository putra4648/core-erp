package id.putra.core_erp.modules.budget.service;

import java.util.List;

import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.budget.repository.BudgetRepo;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class BudgetService implements BaseService<Budget> {

	private final BudgetRepo budgetRepo;

	public BudgetService(BudgetRepo budgetRepo) {
		this.budgetRepo = budgetRepo;
	}

	@Override
	public void save(Budget entity) {
		budgetRepo.persist(entity);
	}

	@Override
	public Budget findById(Long id) {
		return budgetRepo.findById(id);
	}

	@Override
	public void delete(Long id) {
		budgetRepo.softDeleteById(id);
	}

	@Override
	public void update(Budget entity) {
		budgetRepo.getEntityManager().merge(entity);
	}

	@Override
	public List<Budget> findAll(Budget entity, Page page) {
		return budgetRepo.findAll().page(page).list();
	}

	@Override
	public long count() {
		return budgetRepo.count();
	}
}
