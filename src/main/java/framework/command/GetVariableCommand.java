package framework.command;

import framework.command.entity.Command;
import framework.command.holder.CommandHolder;
import framework.command.holder.CommandHolderAware;
import framework.exception.LaboratoryFrameworkException;
import framework.state.ApplicationState;
import framework.state.ApplicationStateAware;
import framework.utils.ConsoleUtils;
import framework.utils.ValidationUtils;
import framework.variable.entity.Variable;
import framework.variable.holder.VariableHolder;
import framework.variable.holder.VariableHolderAware;
import lombok.Setter;

@Setter
public class GetVariableCommand implements RunnableCommand, ApplicationStateAware,
        VariableHolderAware, CommandHolderAware {

    private ApplicationState applicationState;

    private VariableHolder variableHolder;

    private CommandHolder commandHolder;

    @Override
    public void execute(String[] args) {
        assertFieldsArePresent();
        if (args.length != 1) {
            Command runnableCommand = commandHolder.getCommand("get");
            ConsoleUtils.println(runnableCommand.getConstraintViolationMessage());
            return;
        }
        String variableName = args[0];
        Variable variable = variableHolder.getVariableDto(variableName);
        if (variable == null) {
            ConsoleUtils.println("Unknown variable");
            return;
        }
        Object value = applicationState.getVariable(variableName);
        ConsoleUtils.println(String.format("%s = %s", variableName, value));
    }

    private void assertFieldsArePresent() throws LaboratoryFrameworkException {
        ValidationUtils.requireNonNull(variableHolder, "Variable holder must not be null");
        ValidationUtils.requireNonNull(applicationState, "Application state must not be null");
        ValidationUtils.requireNonNull(commandHolder, "Command holder must not be null");
    }
}
