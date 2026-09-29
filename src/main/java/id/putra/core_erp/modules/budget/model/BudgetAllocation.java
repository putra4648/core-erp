package id.putra.core_erp.modules.budget.model;

import java.math.BigInteger;
import java.util.Date;

public class BudgetAllocation {

	private BigInteger actual;
	private Date date;
	private BigInteger plan;

	public BigInteger getActual() {
		return actual;
	}

	public Date getDate() {
		return date;
	}

	public BigInteger getPlan() {
		return plan;
	}

	public void setActual(BigInteger actual) {
		this.actual = actual;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setPlan(BigInteger plan) {
		this.plan = plan;
	}
}
