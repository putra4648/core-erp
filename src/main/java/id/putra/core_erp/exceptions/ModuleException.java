package id.putra.core_erp.exceptions;

public class ModuleException extends RuntimeException {
    public ModuleException(String message) {
        super(message);
    }

    public ModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
