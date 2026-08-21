package id.putra.core_erp.purchaserequisition.enums;

public enum RoleEnum {
    ADMIN("admin"), SUPERADMIN("superadmin");

    private String value;

    private RoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

