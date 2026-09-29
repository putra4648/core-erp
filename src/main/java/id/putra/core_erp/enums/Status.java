package id.putra.core_erp.enums;

public enum Status {
	ACTIVE("ACTIVE"), ALL(""), INACTIVE("INACTIVE");

	private String value;

	private Status(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
