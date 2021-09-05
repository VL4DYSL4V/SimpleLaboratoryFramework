package framework.state;

import framework.utils.ConsoleUtils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public final class StateHelper {

    private StateHelper() {
    }

    public static <T> void defaultSet(String variableName, String expectedName,
                                      Object rawValue, Class<T> expectedClass,
                                      Function<Object, T> classCastingFunction, Consumer<T> actualSetter) {
        if (Objects.equals(expectedName, variableName)) {
            if (Objects.equals(expectedClass, rawValue.getClass())) {
                T casted = classCastingFunction.apply(rawValue);
                actualSetter.accept(casted);
            } else {
                ConsoleUtils.println("Invalid type");
            }
        } else {
            ConsoleUtils.println("Wrong name");
        }
    }

}
