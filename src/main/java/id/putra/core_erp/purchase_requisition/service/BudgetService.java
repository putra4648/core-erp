package id.putra.core_erp.purchase_requisition.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import id.putra.core_erp.purchase_requisition.dto.BudgetDetailDto;
import id.putra.core_erp.purchase_requisition.dto.BudgetDto;

public interface BudgetService {
    Map<String, Object> getBudgets(List<String> deptId) throws ParseException;

    void addOrUpdateBudget(BudgetDto body) throws ParseException;

    List<BudgetDetailDto> getBudgetsDetail(String deptId, String budgetId, String filter);

    BudgetDetailDto getBudgetDetail(String deptId, String budgetId);

    List<BudgetDetailDto> getBudgetsDetail(String deptId, String year);
}
