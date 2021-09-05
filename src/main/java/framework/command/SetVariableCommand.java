package framework.command;

import framework.command.entity.Command;
import framework.command.holder.CommandHolder;
import framework.command.holder.CommandHolderAware;
import framework.enums.VariableType;
import framework.exception.LaboratoryFrameworkException;
import framework.state.ApplicationState;
import framework.state.ApplicationStateAware;
import framework.utils.ConsoleUtils;
import framework.utils.ValidationUtils;
import framework.variable.holder.VariableHolder;
import framework.variable.entity.Variable;
import framework.variable.holder.VariableHolderAware;
import lombok.Setter;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

@Setter
public class SetVariableCommand implements RunnableCommand, VariableHolderAware,
        CommandHolderAware, ApplicationStateAware {

    private CommandHolder commandHolder;

    private VariableHolder variableHolder;

    private ApplicationState applicationState;

    private final Map<VariableType, Supplier<Object>> variableTypeToValueSupplierMap =
            new EnumMap<>(VariableType.class);

    public SetVariableCommand() {
        setValueSuppliers();
    }

    @Override
    public void execute(String[] args) {
        assertFieldsArePresent();
        if (args.length != 1) {
            Command runnableCommand = commandHolder.getCommand("set");
            ConsoleUtils.println(runnableCommand.getConstraintViolationMessage());
            return;
        }
        String variableName = args[0];
        Variable variable = variableHolder.getVariableDto(variableName);
        if (variable == null) {
            ConsoleUtils.println("Unknown variable");
            return;
        }
        Object result = variableTypeToValueSupplierMap.get(variable.getType()).get();
        applicationState.setVariable(variableName, result);
    }

    private void setValueSuppliers() {
        variableTypeToValueSupplierMap.put(VariableType.BIG_DECIMAL, ConsoleUtils::askForBigDecimalRepeatedly);
    }

    private void assertFieldsArePresent() throws LaboratoryFrameworkException {
        ValidationUtils.requireNonNull(variableHolder, "Variable holder must not be null");
        ValidationUtils.requireNonNull(applicationState, "Application state must not be null");
        ValidationUtils.requireNonNull(commandHolder, "Command holder must not be null");
    }
}
