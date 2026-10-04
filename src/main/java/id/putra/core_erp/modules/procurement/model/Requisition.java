package id.putra.core_erp.modules.procurement.model;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.approval.model.Approval;
import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.department.model.Department;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class Requisition extends BaseEntity {
    
    @Column(name = "title")
    private String title;

    @Column(name = "note")
    private String note;

    @Column(name = "reject_note")
    private String rejectNote;

    @Column(name = "currency")
    private BigInteger currency;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "investment_id")
    private String investmentId;

    @Column(name = "unit_price")
    private BigInteger unitPrice;

    @Column(name = "status")
    private String status;

    @Column(name = "budget_detail_id")
    private Long budgetDetailId;

    @Column(name = "budget_date")
    private Date budgetDate;

    @Column(name = "current_approval_stage", columnDefinition = "integer default 0")
    private Integer currentApprovalStage;

    @Column(name = "total_approval_stage")
    private Integer totalApprovalStage;

    @Column(name = "last_approved")
    private String lastApproved;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "temp_approvals", columnDefinition = "jsonb")
    private List<Approval> approvals;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "temp_purchaser_approval", columnDefinition = "jsonb")
    private Approval approval;

    private Approval purchaser;

    @Column(name = "next_approvals")
    private List<String> nextApprovals;

    @ManyToOne
    @JoinColumn(name = "budget_id", referencedColumnName = "id")
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "dept_id", referencedColumnName = "dept_number")
    private Department department;

    @OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequisitionDetail> details;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }

    public BigInteger getCurrency() {
        return currency;
    }

    public void setCurrency(BigInteger currency) {
        this.currency = currency;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getInvestmentId() {
        return investmentId;
    }

    public void setInvestmentId(String investmentId) {
        this.investmentId = investmentId;
    }

    public BigInteger getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigInteger unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getBudgetDetailId() {
        return budgetDetailId;
    }

    public void setBudgetDetailId(Long budgetDetailId) {
        this.budgetDetailId = budgetDetailId;
    }

    public Date getBudgetDate() {
        return budgetDate;
    }

    public void setBudgetDate(Date budgetDate) {
        this.budgetDate = budgetDate;
    }

    public Integer getCurrentApprovalStage() {
        return currentApprovalStage;
    }

    public void setCurrentApprovalStage(Integer currentApprovalStage) {
        this.currentApprovalStage = currentApprovalStage;
    }

    public Integer getTotalApprovalStage() {
        return totalApprovalStage;
    }

    public void setTotalApprovalStage(Integer totalApprovalStage) {
        this.totalApprovalStage = totalApprovalStage;
    }

    public String getLastApproved() {
        return lastApproved;
    }

    public void setLastApproved(String lastApproved) {
        this.lastApproved = lastApproved;
    }

    public List<Approval> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<Approval> temApprovalDtos) {
        this.approvals = temApprovalDtos;
    }

    public Approval getApproval() {
        return approval;
    }

    public void setApproval(Approval tempPurchaserApprovalDto) {
        this.approval = tempPurchaserApprovalDto;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<RequisitionDetail> getDetails() {
        return details;
    }

    public void setDetails(List<RequisitionDetail> details) {
        this.details = details;
    }

    public List<String> getNextApprovals() {
        return nextApprovals;
    }

    public void setNextApprovals(List<String> nextApprovals) {
        this.nextApprovals = nextApprovals;
    }

    public Approval getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(Approval purchaser) {
        this.purchaser = purchaser;
    }

}
