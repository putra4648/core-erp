package id.putra.core_erp.purchase_requisition.exception;

public class RequisitionException extends RuntimeException {
    public RequisitionException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RequisitionException(String message) {
        super(message);
    }
}
