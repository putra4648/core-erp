package id.putra.core_erp.purchase_requisition.dto;

import java.util.List;

import id.putra.core_erp.purchase_requisition.enums.BudgetEnum;

public class BudgetDetailDto {
    private String name;
    private Long id;
    private String note;
    private BudgetEnum type;
    private List<BudgetTypeDto> plans;
    private String total_budget;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BudgetEnum getType() {
        return type;
    }

    public void setType(BudgetEnum type) {
        this.type = type;
    }

    public List<BudgetTypeDto> getPlans() {
        return plans;
    }

    public void setPlans(List<BudgetTypeDto> plans) {
        this.plans = plans;
    }

    public String getTotal_budget() {
        return total_budget;
    }

    public void setTotal_budget(String total_budget) {
        this.total_budget = total_budget;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
