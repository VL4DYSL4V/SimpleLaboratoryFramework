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
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Objects;

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
        Variable variable = variableHolder.getVariable(variableName);
        if (variable == null) {
            ConsoleUtils.println("Unknown variable");
            return;
        }
        try {
            Object value = applicationState.getVariable(variableName);
            printVariable(variableName, value);
        }
        catch (LaboratoryFrameworkException e) {
            ConsoleUtils.println(e.getMessage());
        }
    }

    private static void printVariable(String variableName, Object value) {
        ValidationUtils.requireNonNull(value);
        if (Objects.equals(value.getClass(), Interval.class)) {
            Interval interval = (Interval) value;
            ConsoleUtils.printInterval(interval);
            return;
        }
        if (value instanceof RealMatrix) {
            RealMatrix matrix = (RealMatrix) value;
            ConsoleUtils.printMatrix(16, matrix);
            return;
        }
        if (value instanceof RealVector) {
            RealVector vector = (RealVector) value;
            ConsoleUtils.printVector(16, vector);
            return;
        }
        ConsoleUtils.println(String.format("%s = %s", variableName, value));
    }

    private void assertFieldsArePresent() throws LaboratoryFrameworkException {
        ValidationUtils.requireNonNull(variableHolder, "Variable holder must not be null");
        ValidationUtils.requireNonNull(applicationState, "Application state must not be null");
        ValidationUtils.requireNonNull(commandHolder, "Command holder must not be null");
    }
}
