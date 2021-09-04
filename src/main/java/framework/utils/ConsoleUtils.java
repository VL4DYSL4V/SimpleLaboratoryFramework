package framework.utils;

import framework.exception.LaboratoryFrameworkException;

import java.math.BigDecimal;
import java.util.Scanner;

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
        println("Input a number");
        BigDecimal out = null;
        while (out == null) {
            String nextLine = readLine();
            try {
                out = new BigDecimal(nextLine);
            } catch (Exception e) {
                println("Invalid number format. Try again");
            }
        }
        return out;
    }
}
