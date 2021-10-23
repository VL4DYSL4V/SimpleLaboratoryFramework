package framework.state;

import framework.utils.ConsoleUtils;
import framework.utils.ValidationUtils;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class StateHelper {

    private StateHelper() {
    }

    private static <T> void defaultSet(String variableName, String expectedName,
                                       Object rawValue, Class<T> expectedClass,
                                       Consumer<T> actualSetter) {
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

    public static <T> BiConsumer<String, Object> getDefaultSetter(String expectedName, Class<T> expectedClass, Consumer<T> actualSetter) {
        ValidationUtils.requireNonNull(expectedName, expectedClass, actualSetter);
        return (name, value) -> defaultSet(name, expectedName, value, expectedClass, actualSetter);
    }
}
