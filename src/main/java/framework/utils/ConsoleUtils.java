package framework.utils;

import framework.exception.LaboratoryFrameworkException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;

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

    public static String askForStringRepeatedly() {
        return askForObjectRepeatedly("Input a string (Not empty)", Function.identity(),
                "Invalid string. Try again");
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

    public static ArrayRealVector askForVectorRepeatedly(int vectorLength) {
        ValidationUtils.requireGreaterOrEqualThan(vectorLength, 1, String.format("Vector length must be >= %d", 1));
        println(String.format("Input vector with length: %d", vectorLength));
        double[] values = askForDoubleArrayRepeatedly(vectorLength);
        return new ArrayRealVector(values);
    }

    public static Array2DRowRealMatrix askForMatrixRepeatedly(int rowCount, int columnCount) {
        ValidationUtils.requireGreaterOrEqualThan(rowCount, 1, String.format("Row count must be >= %d", 1));
        ValidationUtils.requireGreaterOrEqualThan(columnCount, 1, String.format("Column count must be >= %d", 1));
        println(String.format("Input matrix with row count: %d and column count: %d", rowCount, columnCount));
        Array2DRowRealMatrix out = new Array2DRowRealMatrix(rowCount, columnCount);
        for (int i = 0; i < rowCount; i++) {
            double[] row = askForDoubleArrayRepeatedly(columnCount);
            out.setRow(i, row);
        }
        return out;
    }

    public static double[] askForDoubleArrayRepeatedly(int length) {
        ValidationUtils.requireGreaterOrEqualThan(length, 1, String.format("Array length must be >= %d", 1));
        String message = String.format("Input %d numbers(double), split by whitespace", length);
        Function<String, double[]> mapper = s -> convertStringToDoubleArray(s, length);
        return askForObjectRepeatedly(message, mapper, "Invalid input");
    }

    private static double[] convertStringToDoubleArray(String s, int length) {
        ValidationUtils.requireGreaterOrEqualThan(length, 1, String.format("Array length must be >= %d", 1));
        String message = String.format("String must contain %d numbers", length);
        final String[] split = s.split(" ");
        if (split.length != length) {
            throw new LaboratoryFrameworkException(message);
        }
        double[] values = new double[split.length];
        for (int i = 0; i < split.length; i++) {
            values[i] = Double.parseDouble(split[i]);
        }
        return values;
    }

    public static <T> T askForObjectRepeatedly(String message, Function<String, T> mapper, String errorMessage) {
        println(message);
        T out = null;
        while (out == null) {
            String nextLine = readLine().trim();
            try {
                out = mapper.apply(nextLine);
            } catch (Exception e) {
                println(errorMessage);
            }
        }
        return out;
    }
}
