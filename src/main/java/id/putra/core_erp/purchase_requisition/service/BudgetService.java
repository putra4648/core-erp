package id.putra.core_erp.purchase_requisition.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import id.putra.core_erp.purchase_requisition.model.Budget;
import id.putra.core_erp.purchase_requisition.model.BudgetDetail;

public interface BudgetService {
    Map<String, Object> getBudgets(List<String> deptId) throws ParseException;

    void addOrUpdateBudget(Budget body) throws ParseException;

    List<BudgetDetail> getBudgetsDetail(String deptId, String budgetId, String filter);

    BudgetDetail getBudgetDetail(String deptId, String budgetId);

    List<BudgetDetail> getBudgetsDetail(String deptId, String year);
}
