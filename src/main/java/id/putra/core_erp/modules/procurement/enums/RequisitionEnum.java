package id.putra.core_erp.modules.procurement.enums;

public enum RequisitionEnum {
    ALL("ALL"),
    SEND("SEND"),
    WAITING_APPROVAL("WAITING_APPROVAL"),
    OPEN("OPEN"),
    PO_CREATED("PO_CREATED"),
    DRAFT("DRAFT"),
    CLOSED("CLOSED"),
    APPROVED("APPROVED"),
    REJECT("REJECT");

    private String value;

    private RequisitionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
