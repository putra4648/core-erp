package id.putra.core_erp.purchase_requisition.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import id.putra.core_erp.purchase_requisition.model.Budget;
import id.putra.core_erp.purchase_requisition.model.Department;

public interface BudgetRepo extends CrudRepository<Budget, String> {

    List<Budget> findByDepartmentInAndFiscalYearAfter(Iterable<Department> department, Date fiscalYear);

    List<Budget> findByDepartmentInAndFiscalYearBefore(Iterable<Department> department, Date fiscalYear);

}
