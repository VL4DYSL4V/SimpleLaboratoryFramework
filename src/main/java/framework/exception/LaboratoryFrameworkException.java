package framework.exception;

/**
 * Class is a superclass for all exception that will be thrown intentionally in this framework
 */
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
