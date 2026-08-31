package id.putra.core_erp.purchase_requisition.enums;

public enum StatusEnum {
    ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), ALL("");

    private String value;

    private StatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
