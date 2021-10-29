package framework.state;

import framework.utils.ConsoleUtils;
import framework.utils.ValidationUtils;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class StateHelper {

    private StateHelper() {
    }

    private static <T> void defaultSet(String variableName, String expectedName,
                                       Object rawValue, Class<? extends T> expectedClass,
                                       Consumer<? super T> actualSetter) {
        if (Objects.equals(expectedName, variableName)) {
            if (rawValue == null) {
                ConsoleUtils.println("Cannot set null");
                return;
            }
            if (expectedClass.isAssignableFrom(rawValue.getClass())) {
                T casted = expectedClass.cast(rawValue);
                actualSetter.accept(casted);
            } else {
                ConsoleUtils.println("Invalid type");
            }
        } else {
            ConsoleUtils.println("Wrong name");
        }
    }

    public static <T> BiConsumer<String, Object> getDefaultSetter(String expectedName, Class<? extends T> expectedClass,
                                                                  Consumer<? super T> actualSetter) {
        ValidationUtils.requireNonNull(expectedName, expectedClass, actualSetter);
        return (name, value) -> defaultSet(name, expectedName, value, expectedClass, actualSetter);
    }

    public static BiConsumer<String, Object> getByteSetter(String expectedName, Consumer<? super Byte> actualSetter) {
        return getDefaultSetter(expectedName, Byte.class, actualSetter);
    }

    public static BiConsumer<String, Object> getShortSetter(String expectedName, Consumer<? super Short> actualSetter) {
        return getDefaultSetter(expectedName, Short.class, actualSetter);
    }

    public static BiConsumer<String, Object> getIntegerSetter(String expectedName, Consumer<? super Integer> actualSetter) {
        return getDefaultSetter(expectedName, Integer.class, actualSetter);
    }

    public static <T> BiConsumer<String, Object> getDefaultLong(String expectedName, Consumer<? super Long> actualSetter) {
        return getDefaultSetter(expectedName, Long.class, actualSetter);
    }

    public static BiConsumer<String, Object> getFloatSetter(String expectedName, Consumer<? super Float> actualSetter) {
        return getDefaultSetter(expectedName, Float.class, actualSetter);
    }

    public static BiConsumer<String, Object> getDoubleSetter(String expectedName, Consumer<? super Double> actualSetter) {
        return getDefaultSetter(expectedName, Double.class, actualSetter);
    }

    public static BiConsumer<String, Object> getBooleanSetter(String expectedName, Consumer<? super Boolean> actualSetter) {
        return getDefaultSetter(expectedName, Boolean.class, actualSetter);
    }

    public static BiConsumer<String, Object> getCharacterSetter(String expectedName, Consumer<? super Character> actualSetter) {
        return getDefaultSetter(expectedName, Character.class, actualSetter);
    }

    public static BiConsumer<String, Object> getStringSetter(String expectedName, Consumer<? super String> actualSetter) {
        return getDefaultSetter(expectedName, String.class, actualSetter);
    }

    public static BiConsumer<String, Object> getRealMatrixSetter(String expectedName, Consumer<? super RealMatrix> actualSetter) {
        return getDefaultSetter(expectedName, RealMatrix.class, actualSetter);
    }

    public static BiConsumer<String, Object> getVectorSetter(String expectedName, Consumer<? super RealVector> actualSetter) {
        return getDefaultSetter(expectedName, RealVector.class, actualSetter);
    }

    public static BiConsumer<String, Object> getIntervalSetter(String expectedName, Consumer<? super Interval> actualSetter) {
        return getDefaultSetter(expectedName, Interval.class, actualSetter);
    }

    public static BiConsumer<String, Object> getBigDecimalSetter(String expectedName, Consumer<? super BigDecimal> actualSetter) {
        return getDefaultSetter(expectedName, BigDecimal.class, actualSetter);
    }

    public static BiConsumer<String, Object> getBigIntegerSetter(String expectedName, Consumer<? super BigInteger> actualSetter) {
        return getDefaultSetter(expectedName, BigInteger.class, actualSetter);
    }

    public static BiConsumer<String, Object> getPolynomialFunctionSetter(String expectedName, Consumer<? super PolynomialFunction> actualSetter) {
        return getDefaultSetter(expectedName, PolynomialFunction.class, actualSetter);
    }

    public static BiConsumer<String, Object> getComplexSetter(String expectedName, Consumer<? super Complex> actualSetter) {
        return getDefaultSetter(expectedName, Complex.class, actualSetter);
    }

}
