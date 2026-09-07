package id.putra.core_erp.purchase_requisition.model;

import java.util.List;

import id.putra.core_erp.purchase_requisition.enums.StatusEnum;
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
public class Department extends BaseModel {

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
    private Users deptManager;

    // @Column(name = "other_managers")
    // private List<String> otherManager;

    @ManyToOne()
    @JoinColumn(name = "dept_purchaser_nrp", referencedColumnName = "nrp")
    private Users deptPurchaser;

    @ManyToOne
    @JoinColumn(name = "approval_president_nrp", referencedColumnName = "nrp")
    private Users approvalPresident;

    @OneToMany(mappedBy = "department", orphanRemoval = true)
    private List<Users> users;

    @OneToMany(mappedBy = "department")
    private List<Budget> budgets;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Approval> approvals;

    @OneToMany(mappedBy = "department")
    private List<Requisition> requisitions;

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

    public Users getDeptManager() {
        return deptManager;
    }

    public void setDeptManager(Users deptManager) {
        this.deptManager = deptManager;
    }

    public Users getDeptPurchaser() {
        return deptPurchaser;
    }

    public void setDeptPurchaser(Users deptPurchaser) {
        this.deptPurchaser = deptPurchaser;
    }

    public Users getApprovalPresident() {
        return approvalPresident;
    }

    public void setApprovalPresident(Users approvalPresident) {
        this.approvalPresident = approvalPresident;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
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
