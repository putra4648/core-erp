package id.putra.core_erp.modules.department.model;

import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.Status;
import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.procurement.model.Requisition;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table
public class Department extends BaseEntity {

	@NotBlank(message = "Department code is required")
	@Column(name = "dept_number", nullable = false)
	public String code;

	@NotBlank(message = "Department name is required")
	@Column(name = "dept_name", nullable = false)
	public String name;

	@Column(name = "status")
	public Status status;

	@Column(name = "dept_manager_nrp")
	public String deptManager;

	@Column(name = "dept_purchaser_nrp")
	public String deptPurchaser;

	@Column(name = "approval_president_nrp")
	public String approvalPresident;

	@OneToMany(mappedBy = "department")
	public List<Budget> budgets;

	@OneToMany(mappedBy = "department")
	public List<Requisition> requisitions;

}
