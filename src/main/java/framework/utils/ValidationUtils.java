package framework.utils;

import framework.exception.LaboratoryFrameworkException;

import java.util.Objects;

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
     * @throws LaboratoryFrameworkException if any of parameters is null
     */
    public static void requireNonNull(Object... objects) throws LaboratoryFrameworkException {
        for (Object o: objects) {
            if (o == null) {
                throw new LaboratoryFrameworkException("One of parameters is null");
            }
        }
    }

    public static void requireTrue(boolean b) throws LaboratoryFrameworkException {
        if (!b) {
            throw new LaboratoryFrameworkException("Given parameter must be true");
        }
    }

    public static void requireFalse(boolean b) throws LaboratoryFrameworkException {
        if (b) {
            throw new LaboratoryFrameworkException("Given parameter must be false");
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

    public static void requireEquals(Object o1, Object o2, String message) throws LaboratoryFrameworkException {
        if (!Objects.equals(o1, o2)) {
            throw new LaboratoryFrameworkException(message);
        }
    }

    public static void requireNotEquals(Object o1, Object o2, String message) throws LaboratoryFrameworkException {
        if (Objects.equals(o1, o2)) {
            throw new LaboratoryFrameworkException(message);
        }
    }
    /**
     * @throws LaboratoryFrameworkException if given parameter is < numberToCompare with supplied message
     *                                      or if obj is null or if numberToCompare is null
     */
    public static <T extends Number & Comparable<T>> void requireGreaterOrEqualThan(T obj, T numberToCompare, String message)
            throws LaboratoryFrameworkException {
        requireNonNull(obj);
        requireNonNull(numberToCompare);
        if (obj.compareTo(numberToCompare) < 0) {
            throw new LaboratoryFrameworkException(message);
        }
    }

    /**
     * @throws LaboratoryFrameworkException if given parameter is <= numberToCompare with supplied message
     *                                      or if obj is null or if numberToCompare is null
     */
    public static <T extends Number & Comparable<T>> void requireGreaterThan(T obj, T numberToCompare, String message)
            throws LaboratoryFrameworkException {
        requireNonNull(obj);
        requireNonNull(numberToCompare);
        if (obj.compareTo(numberToCompare) <= 0) {
            throw new LaboratoryFrameworkException(message);
        }
    }

    /**
     * @throws LaboratoryFrameworkException if given parameter is > numberToCompare with supplied message
     *                                      or if obj is null or if numberToCompare is null
     */
    public static <T extends Number & Comparable<T>> void requireLesserOrEqualThan(T obj, T numberToCompare, String message)
            throws LaboratoryFrameworkException {
        requireNonNull(obj);
        requireNonNull(numberToCompare);
        if (obj.compareTo(numberToCompare) > 0) {
            throw new LaboratoryFrameworkException(message);
        }
    }

    /**
     * @throws LaboratoryFrameworkException if given parameter is >= numberToCompare with supplied message
     *                                      or if obj is null or if numberToCompare is null
     */
    public static <T extends Number & Comparable<T>> void requireLesserThan(T obj, T numberToCompare, String message)
            throws LaboratoryFrameworkException {
        requireNonNull(obj);
        requireNonNull(numberToCompare);
        if (obj.compareTo(numberToCompare) >= 0) {
            throw new LaboratoryFrameworkException(message);
        }
    }

    /**
     * @throws LaboratoryFrameworkException if given parameter is < lowerBound or > upperBound
     *                                      with supplied message
     */
    public static <T extends Number & Comparable<T>> void requireBetweenClosed(T obj, T lowerBound, T upperBound, String message)
            throws LaboratoryFrameworkException {
        requireNonNull(obj);
        requireNonNull(lowerBound);
        requireNonNull(upperBound);
        if (obj.compareTo(lowerBound) < 0 || obj.compareTo(upperBound) > 0) {
            throw new LaboratoryFrameworkException(message);
        }
    }

    /**
     * @throws LaboratoryFrameworkException if given parameter is <= lowerBound or >= upperBound
     *                                      with supplied message
     */
    public static <T extends Number & Comparable<T>> void requireBetweenOpen(T obj, T lowerBound, T upperBound, String message)
            throws LaboratoryFrameworkException {
        requireNonNull(obj);
        requireNonNull(lowerBound);
        requireNonNull(upperBound);
        if (obj.compareTo(lowerBound) <= 0 || obj.compareTo(upperBound) >= 0) {
            throw new LaboratoryFrameworkException(message);
        }
    }

    /**
     * @throws LaboratoryFrameworkException if given parameter is <= lowerBound or > upperBound
     *                                      with supplied message
     */
    public static <T extends Number & Comparable<T>> void requireBetweenHalfOpen(T obj, T lowerBound, T upperBound, String message)
            throws LaboratoryFrameworkException {
        requireNonNull(obj);
        requireNonNull(lowerBound);
        requireNonNull(upperBound);
        if (obj.compareTo(lowerBound) <= 0 || obj.compareTo(upperBound) > 0) {
            throw new LaboratoryFrameworkException(message);
        }
    }

    /**
     * @throws LaboratoryFrameworkException if given parameter is < lowerBound or >= upperBound
     *                                      with supplied message
     */
    public static <T extends Number & Comparable<T>> void requireBetweenHalfClosed(T obj, T lowerBound, T upperBound, String message)
            throws LaboratoryFrameworkException {
        requireNonNull(obj);
        requireNonNull(lowerBound);
        requireNonNull(upperBound);
        if (obj.compareTo(lowerBound) < 0 || obj.compareTo(upperBound) >= 0) {
            throw new LaboratoryFrameworkException(message);
        }
    }
}
