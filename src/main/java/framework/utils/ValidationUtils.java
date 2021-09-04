package framework.utils;

import framework.exception.LaboratoryFrameworkException;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    /**
     * @throws LaboratoryFrameworkException if given parameter is null
     */
    public static void requireNonNull(Object o) throws LaboratoryFrameworkException {
        if (o == null) {
            throw new LaboratoryFrameworkException("Parameter is null");
        }
    }

    /**
     * @throws LaboratoryFrameworkException if given parameter is null with supplied message
     */
    public static void requireNonNull(Object o, String message) throws LaboratoryFrameworkException {
        if (o == null) {
            throw new LaboratoryFrameworkException(message);
        }
    }

    /**
     * @throws LaboratoryFrameworkException if given parameter is null or empty
     */
    public static void requireNotEmpty(String s) throws LaboratoryFrameworkException {
        requireNonNull(s);
        if (s.isEmpty()) {
            throw new LaboratoryFrameworkException("Parameter is empty");
        }
    }

}
