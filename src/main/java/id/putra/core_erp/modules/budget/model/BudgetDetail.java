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

}
