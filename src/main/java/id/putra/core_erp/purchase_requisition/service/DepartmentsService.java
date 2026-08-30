package id.putra.core_erp.purchase_requisition.service;

import java.util.List;
import java.util.Map;

import id.putra.core_erp.purchase_requisition.dto.ApprovalDto;
import id.putra.core_erp.purchase_requisition.dto.DepartmentDto;

public interface DepartmentsService {
    List<DepartmentDto> getDepartmentDropdown();

    Map<String, Object> getDepartments(String type, String name);

    void addDepartment(DepartmentDto body);

    DepartmentDto getDetailDepartment(String id);

    void updateDepartment(DepartmentDto body);

    void updateDepartmentStatus(String id);

    List<ApprovalDto> getDeptApprovals(String deptId);

    ApprovalDto getDeptApprovalPurchaser(String deptId);
}
