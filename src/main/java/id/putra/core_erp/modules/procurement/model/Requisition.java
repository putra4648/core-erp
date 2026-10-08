package id.putra.core_erp.modules.procurement.model;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.department.model.Department;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table
public class Requisition extends BaseEntity {

	@NotBlank(message = "Title is required")
	@Column(name = "title")
	public String title;

	@Column(name = "note")
	public String note;

	@Column(name = "currency")
	public BigInteger currency;

	@NotNull(message = "Due date is required")
	@Column(name = "due_date")
	public LocalDate dueDate;

	public Integer quantity;

	@Column(name = "unit_price")
	public BigInteger unitPrice;

	@Column(name = "status")
	public String status;

	@NotNull(message = "Budget is required")
	@ManyToOne
	@JoinColumn(name = "budget_id", referencedColumnName = "id")
	public Budget budget;

	@NotNull(message = "Department is required")
	@ManyToOne
	@JoinColumn(name = "dept_id", referencedColumnName = "dept_number")
	public Department department;

	@OneToMany(mappedBy = "requisition")
	public List<RequisitionItem> items;

	// TODO: Add supplier here

}
