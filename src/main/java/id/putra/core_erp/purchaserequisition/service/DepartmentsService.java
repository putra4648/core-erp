package id.putra.core_erp.purchaserequisition.service;

import java.util.List;
import java.util.Map;

import id.putra.core_erp.purchaserequisition.dto.ApprovalDto;
import id.putra.core_erp.purchaserequisition.dto.DepartmentDto;

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

