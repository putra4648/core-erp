package id.putra.core_erp.modules.department.model;

import java.util.List;

import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.procurement.model.Approval;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.shared.entity.BaseEntity;
import id.putra.core_erp.shared.enums.StatusEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "mst_depts")
public class Department extends BaseEntity {

    @Id
    private Long id;

    @Column(name = "dept_number", nullable = false)
    private String deptNumber;

    @Column(name = "dept_name", nullable = false)
    private String deptName;

    @Column(name = "status")
    private StatusEnum status;

    @ManyToOne
    @JoinColumn(name = "dept_manager_nrp", referencedColumnName = "nrp")
    private String deptManager;

    // @Column(name = "other_managers")
    // private List<String> otherManager;

    @ManyToOne()
    @JoinColumn(name = "dept_purchaser_nrp", referencedColumnName = "nrp")
    private String deptPurchaser;

    @ManyToOne
    @JoinColumn(name = "approval_president_nrp", referencedColumnName = "nrp")
    private String approvalPresident;

    @OneToMany(mappedBy = "department", orphanRemoval = true)
    private List<String> String;

    @OneToMany(mappedBy = "department")
    private List<Budget> budgets;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Approval> approvals;

    @OneToMany(mappedBy = "department")
    private List<Requisition> requisitions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptNumber() {
        return deptNumber;
    }

    public void setDeptNumber(String deptNumber) {
        this.deptNumber = deptNumber;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getDeptManager() {
        return deptManager;
    }

    public void setDeptManager(String deptManager) {
        this.deptManager = deptManager;
    }

    public String getDeptPurchaser() {
        return deptPurchaser;
    }

    public void setDeptPurchaser(String deptPurchaser) {
        this.deptPurchaser = deptPurchaser;
    }

    public String getApprovalPresident() {
        return approvalPresident;
    }

    public void setApprovalPresident(String approvalPresident) {
        this.approvalPresident = approvalPresident;
    }

    public List<String> getString() {
        return String;
    }

    public void setString(List<String> String) {
        this.String = String;
    }

    public List<Budget> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    public List<Approval> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<Approval> approvals) {
        this.approvals = approvals;
    }

    public List<Requisition> getRequisitions() {
        return requisitions;
    }

    public void setRequisitions(List<Requisition> requisitions) {
        this.requisitions = requisitions;
    }
}
