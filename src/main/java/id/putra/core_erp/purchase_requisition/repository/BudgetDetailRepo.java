package id.putra.core_erp.purchase_requisition.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import id.putra.core_erp.purchase_requisition.model.BudgetDetail;

public interface BudgetDetailRepo extends CrudRepository<BudgetDetail, String> {
        @Query(nativeQuery = true, value = "select bd.* from budget b join budget_detail bd on b.id = bd.budget_id where b.department = ?1 and b.id = ?2 and bd.type like '%' || ?3 || '%' order by bd.type ")
        List<BudgetDetail> findAllBudgetDetailsByDeptIdAndBudgetIdAndType(String deptId, String budgetId, String type);

        @Query(nativeQuery = true, value = "select bd.* from budget b join budget_detail bd on b.id = bd.budget_id where b.department = ?1 and to_char(b.fy, 'YYYY') = ?2 order by bd.type ")
        List<BudgetDetail> findAllBudgetDetailsByDeptIdAndYear(String deptId, String year);
}
