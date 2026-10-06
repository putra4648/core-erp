package id.putra.core_erp.modules.budget.repository;

import java.util.List;
import java.util.Optional;

import id.putra.core_erp.modules.budget.model.BudgetDetail;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BudgetDetailRepo implements PanacheRepository<BudgetDetail> {

	public List<BudgetDetail> findAllBudgetDetailsByDeptIdAndBudgetIdAndType(String deptId, String budgetId,
			String type) {
		return find(
				"from budget b join budget_detail bd on b.id = bd.budget_id where b.department = ?1 and b.id = ?2 and bd.type like '%' || ?3 || '%' order by bd.type",
				deptId, budgetId, type).list();
	}

	public List<BudgetDetail> findAllBudgetDetailsByDeptIdAndYear(String deptId, String year) {
		return find(
				"from budget b join budget_detail bd on b.id = bd.budget_id where b.department = ?1 and to_char(b.fy, 'YYYY') = ?2 order by bd.type",
				deptId, year).list();
	}

	public Optional<BudgetDetail> findByBudgetId(String id) {
		return find("budget = ?1", id).firstResultOptional();
	}
}
