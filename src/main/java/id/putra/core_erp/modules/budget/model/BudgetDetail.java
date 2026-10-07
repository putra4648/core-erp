package id.putra.core_erp.modules.budget.model;

import java.math.BigInteger;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.BudgetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "budget_detail")
public class BudgetDetail extends BaseEntity {

	@Column(name = "name")
	public String name;

	@Column(name = "type")
	public BudgetType type;

	@Column(name = "note")
	public String note;

	public BigInteger amount;

	@ManyToOne
	@JoinColumn(name = "budget_id", nullable = false)
	public Budget budget;

}
