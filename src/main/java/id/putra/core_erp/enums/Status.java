package id.putra.core_erp.enums;

public enum Status {
<<<<<<< HEAD
	ACTIVE("ACTIVE"), ALL(""), INACTIVE("INACTIVE");
=======
	ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), ALL("");
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	private String value;

	private Status(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
