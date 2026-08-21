package id.putra.core_erp.purchaserequisition.exception;

public class TransferException extends RuntimeException {
    public TransferException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public TransferException(String message) {
        super(message);
    }
}

