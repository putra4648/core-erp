package id.putra.core_erp.modules.procurement.enums;

public enum BudgetEnum {
    ALL(""), EXPENSE("expense"), INVESTMENT("investment");

    private String value;

    private BudgetEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
