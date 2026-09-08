package id.putra.core_erp.purchase_requisition.model;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import id.putra.core_erp.purchase_requisition.dto.BudgetTypeDto;
import id.putra.core_erp.purchase_requisition.enums.BudgetEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "budget_detail")
public class BudgetDetail extends BaseModel {

    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private BudgetEnum type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "budgets")
    private List<BudgetTypeDto> details;

    @Column(name = "note")
    private String note;

    @ManyToOne(optional = false)
    @JoinColumn(name = "budget_id", referencedColumnName = "id")
    private Budget budget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BudgetEnum getType() {
        return type;
    }

    public void setType(BudgetEnum type) {
        this.type = type;
    }

    public List<BudgetTypeDto> getDetails() {
        return details;
    }

    public void setDetails(List<BudgetTypeDto> details) {
        this.details = details;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }
}
