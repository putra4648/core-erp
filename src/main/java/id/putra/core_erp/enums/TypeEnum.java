package id.putra.core_erp.enums;

public enum TypeEnum {
<<<<<<< HEAD
	NON_TRADE("NON_TRADE"), TRADE("TRADE");
=======
	TRADE("TRADE"), NON_TRADE("NON_TRADE");
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	private final String value;

	TypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
