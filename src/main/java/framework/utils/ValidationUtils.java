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

    /**
     * @throws LaboratoryFrameworkException if given parameter is null or empty with supplied message
     */
    public static void requireNotEmpty(String s, String message) throws LaboratoryFrameworkException {
        requireNonNull(s, message);
        if (s.isEmpty()) {
            throw new LaboratoryFrameworkException(message);
        }
    }

    /**
     * @throws LaboratoryFrameworkException if given parameter is less than zero with supplied message
     */
    public static void requireGreaterOrEqualThanZero(int i, String message) throws LaboratoryFrameworkException {
        requireGreaterOrEqualThan(i, 0, message);
    }

    /**
     * @throws LaboratoryFrameworkException if given parameter is less than numberToCompare with supplied message
     */
    public static void requireGreaterOrEqualThan(int i, int numberToCompare, String message) throws LaboratoryFrameworkException {
        if (i < numberToCompare) {
            throw new LaboratoryFrameworkException(message);
        }
    }

    /**
     * @throws LaboratoryFrameworkException if given parameter is less than lowerBound or greater than upperBound
     *                                      with supplied message
     */
    public static void requireBetweenClosed(int i, int lowerBound, int upperBound, String message) throws LaboratoryFrameworkException {
        if (i < lowerBound || i > upperBound) {
            throw new LaboratoryFrameworkException(message);
        }
    }

}
