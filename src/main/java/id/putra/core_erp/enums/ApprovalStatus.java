package id.putra.core_erp.enums;

public enum ApprovalStatus {

	NEED_APPROVAL("NEED_APPROVAL"), IS_PURCHASER("IS_PURCHASER"), ALREADY_APPROVED("ALREADY_APPROVED"),
	NON_APPROVAL("NON_APPROVAL");

	private String value;

	private ApprovalStatus(String value) {
		this.value = value;

	}

	public String getValue() {
		return value;
	}

}
