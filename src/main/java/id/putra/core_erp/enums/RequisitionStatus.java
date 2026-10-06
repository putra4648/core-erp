package id.putra.core_erp.enums;

public enum RequisitionStatus {
<<<<<<< HEAD
	ALL("ALL"), APPROVED("APPROVED"), CLOSED("CLOSED"), DRAFT("DRAFT"), OPEN("OPEN"), PO_CREATED("PO_CREATED"),
	REJECT("REJECT"), SEND("SEND"), WAITING_APPROVAL("WAITING_APPROVAL");
=======
	ALL("ALL"), SEND("SEND"), WAITING_APPROVAL("WAITING_APPROVAL"), OPEN("OPEN"), PO_CREATED("PO_CREATED"),
	DRAFT("DRAFT"), CLOSED("CLOSED"), APPROVED("APPROVED"), REJECT("REJECT");
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	private String value;

	private RequisitionStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
