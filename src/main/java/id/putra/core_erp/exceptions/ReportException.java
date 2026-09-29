package id.putra.core_erp.exceptions;

public class ReportException extends RuntimeException {

	public ReportException(String message) {
		super(message);
	}

	public ReportException(String message, Throwable cause) {
		super(message, cause);
	}

}
