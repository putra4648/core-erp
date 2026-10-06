package id.putra.core_erp.modules.budget.model;

import java.math.BigInteger;
import java.util.Date;

public class BudgetAllocation {

<<<<<<< HEAD
	private BigInteger actual;
	private Date date;
	private BigInteger plan;

	public BigInteger getActual() {
		return actual;
	}

	public Date getDate() {
		return date;
=======
	private Date date;
	private BigInteger plan;
	private BigInteger actual;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	public BigInteger getPlan() {
		return plan;
	}

<<<<<<< HEAD
	public void setActual(BigInteger actual) {
		this.actual = actual;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setPlan(BigInteger plan) {
		this.plan = plan;
=======
	public void setPlan(BigInteger plan) {
		this.plan = plan;
	}

	public BigInteger getActual() {
		return actual;
	}

	public void setActual(BigInteger actual) {
		this.actual = actual;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}
}
