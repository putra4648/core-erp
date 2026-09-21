package id.putra.core_erp.enums;

public enum TypeEnum {
    TRADE("TRADE"), NON_TRADE("NON_TRADE");

    private final String value;

    TypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
