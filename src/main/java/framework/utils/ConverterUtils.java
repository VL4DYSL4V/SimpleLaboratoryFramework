package framework.utils;

import framework.exception.LaboratoryFrameworkException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

public final class ConverterUtils {

    private ConverterUtils() {
    }

    public static Byte byteFromString(String s) throws LaboratoryFrameworkException {
        return convert(s, Byte::parseByte);
    }

    public static Short shortFromString(String s) throws LaboratoryFrameworkException {
        return convert(s, Short::parseShort);
    }

    public static Integer integerFromString(String s) throws LaboratoryFrameworkException {
        return convert(s, Integer::parseInt);
    }

    public static Long longFromString(String s) throws LaboratoryFrameworkException {
        return convert(s, Long::parseLong);
    }

    public static Boolean booleanFromString(String s) throws LaboratoryFrameworkException {
        return convert(s, Boolean::parseBoolean);
    }

    public static Character characterFromString(String s) throws LaboratoryFrameworkException {
        return convert(s, str -> {
            if (str.length() < 1) {
                throw new LaboratoryFrameworkException("String length must be 1 or more");
            }
            return str.charAt(0);
        });
    }

    public static Float floatFromString(String s) throws LaboratoryFrameworkException {
        return convert(s, Float::parseFloat);
    }

    public static Double doubleFromString(String s) throws LaboratoryFrameworkException {
        return convert(s, Double::parseDouble);
    }

    public static BigInteger bigIntegerFromString(String s) throws LaboratoryFrameworkException {
        return convert(s, BigInteger::new);
    }

    public static BigDecimal bigDecimalFromString(String s) throws LaboratoryFrameworkException {
        return convert(s, BigDecimal::new);
    }

    private static <T> T convert(String s, Function<String, T> mapper) throws LaboratoryFrameworkException {
        ValidationUtils.requireNotEmpty(s);
        ValidationUtils.requireNonNull(mapper);
        try {
            return mapper.apply(s);
        } catch (NumberFormatException e) {
            throw new LaboratoryFrameworkException(String.format("Supplied string '%s' cannot be converted", s));
        }
    }

}
