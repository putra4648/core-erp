package id.putra.core_erp.modules.budget.model;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BudgetType {
    @JsonAlias("budget_date")
    @JsonProperty("budget_date")
    private String budgetDateStr;
    private BigInteger plan;
    private BigInteger actual;

    public String getBudgetDateStr() {
        return budgetDateStr;
    }

    public void setBudgetDateStr(String budgetDateStr) {
        this.budgetDateStr = budgetDateStr;
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
