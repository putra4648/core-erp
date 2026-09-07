package id.putra.core_erp.purchase_requisition.dto;

import java.util.List;

import id.putra.core_erp.purchase_requisition.enums.StatusEnum;

public class DepartmentDto {
    private Long id;
    private String dept_number;
    private String dept_name;
    private StatusEnum status;
    private UsersDto dept_manager;
    private List<ApprovalDto> approvals;
    private UsersDto dept_purchaser;
    private UsersDto approval_president;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDept_number() {
        return dept_number;
    }

    public void setDept_number(String dept_number) {
        this.dept_number = dept_number;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public UsersDto getDept_manager() {
        return dept_manager;
    }

    public void setDept_manager(UsersDto dept_manager) {
        this.dept_manager = dept_manager;
    }

    public List<ApprovalDto> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<ApprovalDto> approvals) {
        this.approvals = approvals;
    }

    public UsersDto getDept_purchaser() {
        return dept_purchaser;
    }

    public void setDept_purchaser(UsersDto dept_purchaser) {
        this.dept_purchaser = dept_purchaser;
    }

    public UsersDto getApproval_president() {
        return approval_president;
    }

    public void setApproval_president(UsersDto approval_president) {
        this.approval_president = approval_president;
    }
}
