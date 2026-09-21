package id.putra.core_erp.modules.budget.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.budget.model.BudgetDetail;

public interface BudgetService {
    Map<String, Object> getBudgets(List<String> deptId) throws ParseException;

    void addOrUpdateBudget(Budget body) throws ParseException;

    List<BudgetDetail> getBudgetsDetail(String deptId, String budgetId, String filter);

    BudgetDetail getBudgetDetail(String deptId, String budgetId);

    List<BudgetDetail> getBudgetsDetail(String deptId, String year);
}
