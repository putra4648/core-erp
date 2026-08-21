package id.putra.core_erp.purchaserequisition.exception;

public class InventoryException extends RuntimeException {
    public InventoryException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public InventoryException(String message) {
        super(message);
    }
}

