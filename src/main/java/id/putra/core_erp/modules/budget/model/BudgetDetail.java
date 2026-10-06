package id.putra.core_erp.modules.budget.model;

import java.math.BigInteger;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.BudgetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "budget_detail")
public class BudgetDetail extends BaseEntity {

<<<<<<< HEAD
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
=======
	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private BudgetType type;

	@Column(name = "note")
	private String note;

	private BigInteger amount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BudgetType getType() {
		return type;
	}

	public void setType(BudgetType type) {
		this.type = type;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigInteger getAmount() {
		return amount;
	}

	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}

>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
}
