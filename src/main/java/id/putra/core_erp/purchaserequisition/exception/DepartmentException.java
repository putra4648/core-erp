package id.putra.core_erp.purchaserequisition.exception;

public class DepartmentException extends RuntimeException {
    public DepartmentException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DepartmentException(String message) {
        super(message);
    }
}

