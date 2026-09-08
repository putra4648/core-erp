package id.putra.core_erp.purchase_requisition.service;

import java.util.List;
import java.util.Map;

import id.putra.core_erp.purchase_requisition.model.Approval;
import id.putra.core_erp.purchase_requisition.model.Department;

public interface DepartmentsService {
    List<Department> getDepartmentDropdown();

    Map<String, Object> getDepartments(String type, String name);

    void addDepartment(Department body);

    Department getDetailDepartment(Long id);

    void updateDepartment(Department body);

    void updateDepartmentStatus(Long id);

    List<Approval> getDeptApprovals(Long deptId);

    Approval getDeptApprovalPurchaser(Long deptId);
}
