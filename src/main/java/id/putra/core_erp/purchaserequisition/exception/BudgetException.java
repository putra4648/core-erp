package id.putra.core_erp.purchaserequisition.exception;

public class BudgetException extends RuntimeException {
    public BudgetException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BudgetException(String message) {
        super(message);
    }
}

