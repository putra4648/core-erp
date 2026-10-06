package id.putra.core_erp.enums;

public enum Status {
	ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), ALL("");

	private String value;

	private Status(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
