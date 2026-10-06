package id.putra.core_erp.enums;

public enum ApprovalStatus {

<<<<<<< HEAD
	ALREADY_APPROVED("ALREADY_APPROVED"), IS_PURCHASER("IS_PURCHASER"), NEED_APPROVAL("NEED_APPROVAL"),
=======
	NEED_APPROVAL("NEED_APPROVAL"), IS_PURCHASER("IS_PURCHASER"), ALREADY_APPROVED("ALREADY_APPROVED"),
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	NON_APPROVAL("NON_APPROVAL");

	private String value;

	private ApprovalStatus(String value) {
		this.value = value;

	}

	public String getValue() {
		return value;
	}

}
