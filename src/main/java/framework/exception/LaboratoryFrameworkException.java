package framework.exception;

public class LaboratoryFrameworkException extends RuntimeException {

    public LaboratoryFrameworkException() {
    }

    public LaboratoryFrameworkException(String message) {
        super(message);
    }

    public LaboratoryFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public LaboratoryFrameworkException(Throwable cause) {
        super(cause);
    }

    public LaboratoryFrameworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
