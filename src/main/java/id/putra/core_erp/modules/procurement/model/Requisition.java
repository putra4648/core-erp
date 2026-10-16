package id.putra.core_erp.modules.procurement.model;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.department.model.Department;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

	@Column(name = "reference_id", nullable = false)
	public String referenceId;

	@Column(name = "note")
	public String note;

	@NotNull(message = "Due date is required")
	@Column(name = "due_date")
	public LocalDate dueDate;

	@Column(name = "total_amount")
	public BigInteger totalAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	public ApprovalStatus status;

	@NotNull(message = "Budget is required")
	@ManyToOne
	@JoinColumn(name = "budget_id")
	public Budget budget;

	@NotNull(message = "Department is required")
	@ManyToOne
	@JoinColumn(name = "dept_id")
	public Department department;

	@OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	public List<RequisitionItem> items = new ArrayList<>();

	@Override
	public String toString() {
		return "Requisition [title=" + title + ", referenceId=" + referenceId + ", note=" + note
				+ ", dueDate=" + dueDate + ", totalAmount=" + totalAmount
				+ ", status=" + status + ", budget=" + budget + ", department=" + department + ", items=" + items + "]";
	}

}
