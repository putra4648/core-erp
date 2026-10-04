package id.putra.core_erp.modules.budget.model;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.BudgetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "budget_detail")
public class BudgetDetail extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private BudgetType type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "budgets")
    private List<BudgetAllocation> allocations;

    @Column(name = "note")
    private String note;


    private BigInteger totalBudget;

    public BigInteger getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigInteger totalBudget) {
        this.totalBudget = totalBudget;
    }


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

    public List<BudgetAllocation> getAllocations() {
        return allocations;
    }

    public void setAllocations(List<BudgetAllocation> details) {
        this.allocations = details;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

   
}
