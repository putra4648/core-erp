package id.putra.core_erp.enums;

public enum TypeEnum {
	NON_TRADE("NON_TRADE"), TRADE("TRADE");

	private final String value;

	TypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
