package id.putra.core_erp.modules.budget.model;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.BudgetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "budget_detail")
public class BudgetDetail extends BaseEntity {

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb", name = "budgets")
	private List<BudgetAllocation> allocations;

	@ManyToOne(optional = false)
	@JoinColumn(name = "budget_id", referencedColumnName = "id")
	private Budget budget;

	@Id
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "note")
	private String note;

	private BigInteger totalBudget;

	@Column(name = "type")
	private BudgetType type;

	public List<BudgetAllocation> getAllocations() {
		return allocations;
	}

	public Budget getBudget() {
		return budget;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNote() {
		return note;
	}

	public BigInteger getTotalBudget() {
		return totalBudget;
	}

	public BudgetType getType() {
		return type;
	}

	public void setAllocations(List<BudgetAllocation> details) {
		this.allocations = details;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setTotalBudget(BigInteger totalBudget) {
		this.totalBudget = totalBudget;
	}

	public void setType(BudgetType type) {
		this.type = type;
	}
}
