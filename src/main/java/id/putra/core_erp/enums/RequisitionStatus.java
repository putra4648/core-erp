package id.putra.core_erp.enums;

public enum RequisitionStatus {
	ALL("ALL"), APPROVED("APPROVED"), CLOSED("CLOSED"), DRAFT("DRAFT"), OPEN("OPEN"), PO_CREATED("PO_CREATED"),
	REJECT("REJECT"), SEND("SEND"), WAITING_APPROVAL("WAITING_APPROVAL");

	private String value;

	private RequisitionStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
