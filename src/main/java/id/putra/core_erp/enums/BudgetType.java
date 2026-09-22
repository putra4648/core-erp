package id.putra.core_erp.enums;

public enum BudgetType {
    ALL(""), EXPENSE("expense"), INVESTMENT("investment");

    private String value;

    private BudgetType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
