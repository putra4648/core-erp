package id.putra.core_erp.modules.department.model;

import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.Status;
import id.putra.core_erp.modules.approval.model.Approval;
import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.procurement.model.Requisition;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "mst_depts")
public class Department extends BaseEntity {

	@Column(name = "approval_president_nrp")
	private String approvalPresident;

	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Approval> approvals;

	@OneToMany(mappedBy = "department")
	private List<Budget> budgets;

	@Column(name = "dept_number", nullable = false)
	private String code;

	@Column(name = "dept_manager_nrp")
	private String deptManager;

	// @Column(name = "other_managers")
	// private List<String> otherManager;

	@Column(name = "dept_purchaser_nrp")
	private String deptPurchaser;

	@Id
	private Long id;

	@Column(name = "dept_name", nullable = false)
	private String name;

	@OneToMany(mappedBy = "department")
	private List<Requisition> requisitions;

	@Column(name = "status")
	private Status status;

	public Department() {

	}

	public Department(Long id, String code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public String getApprovalPresident() {
		return approvalPresident;
	}

	public List<Approval> getApprovals() {
		return approvals;
	}

	public List<Budget> getBudgets() {
		return budgets;
	}

	public String getCode() {
		return code;
	}

	public String getDeptManager() {
		return deptManager;
	}

	public String getDeptPurchaser() {
		return deptPurchaser;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Requisition> getRequisitions() {
		return requisitions;
	}

	public Status getStatus() {
		return status;
	}

	public void setApprovalPresident(String approvalPresident) {
		this.approvalPresident = approvalPresident;
	}

	public void setApprovals(List<Approval> approvals) {
		this.approvals = approvals;
	}

	public void setBudgets(List<Budget> budgets) {
		this.budgets = budgets;
	}

	public void setCode(String deptNumber) {
		this.code = deptNumber;
	}

	public void setDeptManager(String deptManager) {
		this.deptManager = deptManager;
	}

	public void setDeptPurchaser(String deptPurchaser) {
		this.deptPurchaser = deptPurchaser;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String deptName) {
		this.name = deptName;
	}

	public void setRequisitions(List<Requisition> requisitions) {
		this.requisitions = requisitions;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
