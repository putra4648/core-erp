package id.putra.core_erp.enums;

public enum RequisitionStatus {
	ALL("ALL"), SEND("SEND"), WAITING_APPROVAL("WAITING_APPROVAL"), OPEN("OPEN"), PO_CREATED("PO_CREATED"),
	DRAFT("DRAFT"), CLOSED("CLOSED"), APPROVED("APPROVED"), REJECT("REJECT");

	private String value;

	private RequisitionStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
