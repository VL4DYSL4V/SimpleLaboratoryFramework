package framework.utils;

import framework.exception.LaboratoryFrameworkException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.function.Function;

public final class ConsoleUtils {

    private ConsoleUtils() {
    }

    /**
     * Prints string to system output stream
     *
     * @throws LaboratoryFrameworkException if given string is null
     */
    public static void print(String s) throws LaboratoryFrameworkException {
        ValidationUtils.requireNonNull(s);
        System.out.print(s);
    }

    /**
     * Prints string to system output stream with line breaking char at the end
     *
     * @throws LaboratoryFrameworkException if given string is null
     */
    public static void println(String s) throws LaboratoryFrameworkException {
        ValidationUtils.requireNonNull(s);
        System.out.println(s);
    }

    public static String readLine() {
        return new Scanner(System.in).nextLine();
    }

    public static BigDecimal askForBigDecimalRepeatedly() {
        return askForObjectRepeatedly("Input a number (BigDecimal)", BigDecimal::new,
                "Invalid number format. Try again");
    }

    public static BigInteger askForBigBigIntegerRepeatedly() {
        return askForObjectRepeatedly("Input a number (BigInteger)", BigInteger::new,
                "Invalid number format. Try again");
    }

    public static Byte askForByteRepeatedly() {
        return askForObjectRepeatedly("Input a number (Byte)", Byte::parseByte,
                "Invalid number format. Try again");
    }

    public static Short askForShortRepeatedly() {
        return askForObjectRepeatedly("Input a number (Short)", Short::parseShort,
                "Invalid number format. Try again");
    }

    public static Integer askForIntegerRepeatedly() {
        return askForObjectRepeatedly("Input a number (Integer)", Integer::parseInt,
                "Invalid number format. Try again");
    }

    public static Long askForLongRepeatedly() {
        return askForObjectRepeatedly("Input a number (Long)", Long::parseLong,
                "Invalid number format. Try again");
    }

    public static Boolean askForBooleanRepeatedly() {
        return askForObjectRepeatedly("Input a number (Boolean)", Boolean::parseBoolean,
                "Invalid number format. Try again");
    }

    public static Character askForCharacterRepeatedly() {
        return askForObjectRepeatedly("Input a number (Character)",
                s -> {
                    ValidationUtils.requireNotEmpty(s);
                    return s.charAt(0);
                },
                "Invalid number format. Try again");
    }

    public static Float askForFloatRepeatedly() {
        return askForObjectRepeatedly("Input a number (Float)", Float::parseFloat,
                "Invalid number format. Try again");
    }

    public static Double askForDoubleRepeatedly() {
        return askForObjectRepeatedly("Input a number (Double)", Double::parseDouble,
                "Invalid number format. Try again");
    }

    private static <T> T askForObjectRepeatedly(String message, Function<String, T> objectFromStringFunction, String errorMessage) {
        println(message);
        T out = null;
        while (out == null) {
            String nextLine = readLine().trim();
            try {
                out = objectFromStringFunction.apply(nextLine);
            } catch (Exception e) {
                println(errorMessage);
            }
        }
        return out;
    }
}
