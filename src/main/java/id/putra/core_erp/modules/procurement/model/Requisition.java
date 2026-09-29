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
@Table(name = "requisition")
public class Requisition extends BaseEntity {
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "temp_purchaser_approval", columnDefinition = "jsonb")
	private Approval approval;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "temp_approvals", columnDefinition = "jsonb")
	private List<Approval> approvals;

	@ManyToOne
	@JoinColumn(name = "budget_id", referencedColumnName = "id")
	private Budget budget;

	@Column(name = "budget_date")
	private Date budgetDate;

	@Column(name = "budget_detail_id")
	private Long budgetDetailId;

	@Column(name = "currency")
	private BigInteger currency;

	@Column(name = "current_approval_stage", columnDefinition = "integer default 0")
	private Integer currentApprovalStage;

	@ManyToOne
	@JoinColumn(name = "dept_id", referencedColumnName = "dept_number")
	private Department department;

	@OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RequisitionDetail> details;

	@Column(name = "due_date")
	private Date dueDate;

	@Id
	private Long id;

	@Column(name = "investment_id")
	private String investmentId;

	@Column(name = "last_approved")
	private String lastApproved;

	@Column(name = "next_approvals")
	private List<String> nextApprovals;

	@Column(name = "note")
	private String note;

	private Approval purchaser;

	@Column(name = "reject_note")
	private String rejectNote;

	@Column(name = "status")
	private String status;

	@Column(name = "title")
	private String title;

	@Column(name = "total_approval_stage")
	private Integer totalApprovalStage;

	@Column(name = "unit_price")
	private BigInteger unitPrice;

	public Approval getApproval() {
		return approval;
	}

	public List<Approval> getApprovals() {
		return approvals;
	}

	public Budget getBudget() {
		return budget;
	}

	public Date getBudgetDate() {
		return budgetDate;
	}

	public Long getBudgetDetailId() {
		return budgetDetailId;
	}

	public BigInteger getCurrency() {
		return currency;
	}

	public Integer getCurrentApprovalStage() {
		return currentApprovalStage;
	}

	public Department getDepartment() {
		return department;
	}

	public List<RequisitionDetail> getDetails() {
		return details;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public Long getId() {
		return id;
	}

	public String getInvestmentId() {
		return investmentId;
	}

	public String getLastApproved() {
		return lastApproved;
	}

	public List<String> getNextApprovals() {
		return nextApprovals;
	}

	public String getNote() {
		return note;
	}

	public Approval getPurchaser() {
		return purchaser;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public String getStatus() {
		return status;
	}

	public String getTitle() {
		return title;
	}

	public Integer getTotalApprovalStage() {
		return totalApprovalStage;
	}

	public BigInteger getUnitPrice() {
		return unitPrice;
	}

	public void setApproval(Approval tempPurchaserApprovalDto) {
		this.approval = tempPurchaserApprovalDto;
	}

	public void setApprovals(List<Approval> temApprovalDtos) {
		this.approvals = temApprovalDtos;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public void setBudgetDate(Date budgetDate) {
		this.budgetDate = budgetDate;
	}

	public void setBudgetDetailId(Long budgetDetailId) {
		this.budgetDetailId = budgetDetailId;
	}

	public void setCurrency(BigInteger currency) {
		this.currency = currency;
	}

	public void setCurrentApprovalStage(Integer currentApprovalStage) {
		this.currentApprovalStage = currentApprovalStage;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void setDetails(List<RequisitionDetail> details) {
		this.details = details;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setInvestmentId(String investmentId) {
		this.investmentId = investmentId;
	}

	public void setLastApproved(String lastApproved) {
		this.lastApproved = lastApproved;
	}

	public void setNextApprovals(List<String> nextApprovals) {
		this.nextApprovals = nextApprovals;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setPurchaser(Approval purchaser) {
		this.purchaser = purchaser;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTotalApprovalStage(Integer totalApprovalStage) {
		this.totalApprovalStage = totalApprovalStage;
	}

	public void setUnitPrice(BigInteger unitPrice) {
		this.unitPrice = unitPrice;
	}

}
