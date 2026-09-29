package id.putra.core_erp.enums;

public enum ApprovalStatus {

	ALREADY_APPROVED("ALREADY_APPROVED"), IS_PURCHASER("IS_PURCHASER"), NEED_APPROVAL("NEED_APPROVAL"),
	NON_APPROVAL("NON_APPROVAL");

	private String value;

	private ApprovalStatus(String value) {
		this.value = value;

	}

	public String getValue() {
		return value;
	}

}
