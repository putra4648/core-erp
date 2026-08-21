package id.putra.core_erp.purchaserequisition.exception;

public class RFQException extends RuntimeException {
    public RFQException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RFQException(String message) {
        super(message);
    }
}

