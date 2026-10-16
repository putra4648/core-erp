package id.putra.core_erp.modules.budget.model;

import java.math.BigDecimal;
import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.BudgetType;
import id.putra.core_erp.modules.department.model.Department;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "budget")
public class Budget extends BaseEntity {

	@ManyToOne(optional = false)
	@JoinColumn(name = "department", referencedColumnName = "dept_number")
	public Department department;

	@NotEmpty
	@Column(name = "name", nullable = false)
	public String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	public BudgetType type;

	@NotNull
	@Column(name = "fiscal_year", nullable = false)
	public Integer fiscalYear;

	@NotNull
	@Column(name = "allocated", nullable = false)
	public BigDecimal allocated;

}
