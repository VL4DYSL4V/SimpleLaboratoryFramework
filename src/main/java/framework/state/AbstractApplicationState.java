package framework.state;

import framework.exception.LaboratoryFrameworkException;
import framework.utils.ConsoleUtils;
import framework.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class AbstractApplicationState implements ApplicationState {

    protected final Map<String, BiConsumer<String, Object>> variableNameToSetter = new HashMap<>();

    protected final Map<String, Supplier<Object>> variableNameToGetter = new HashMap<>();

    public AbstractApplicationState() {
        initVariableNameToSettersMap();
        initVariableNameToGettersMap();
    }

    @Override
    public void setVariable(String variableName, Object value) {
        final BiConsumer<String, Object> setter = variableNameToSetter.get(variableName);
        if (setter == null) {
            ConsoleUtils.println(String.format("Unknown variable name: %s", variableName));
            return;
        }
        setter.accept(variableName, value);
    }

    protected void initVariableNameToSettersMap(){}

    @Override
    public Object getVariable(String variableName) throws LaboratoryFrameworkException {
        final Supplier<Object> getter = variableNameToGetter.get(variableName);
        ValidationUtils.requireNonNull(getter, String.format("Unknown variable name: %s", variableName));
        return getter.get();
    }

    protected void initVariableNameToGettersMap(){}
}
