package framework.utils;

import framework.exception.LaboratoryFrameworkException;

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

}
