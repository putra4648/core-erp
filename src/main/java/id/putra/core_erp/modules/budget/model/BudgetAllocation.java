package id.putra.core_erp.modules.budget.model;

import java.math.BigInteger;
import java.util.Date;

public class BudgetAllocation {

    private Date date;
    private BigInteger plan;
    private BigInteger actual;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigInteger getPlan() {
        return plan;
    }

    public void setPlan(BigInteger plan) {
        this.plan = plan;
    }

    public BigInteger getActual() {
        return actual;
    }

    public void setActual(BigInteger actual) {
        this.actual = actual;
    }
}
