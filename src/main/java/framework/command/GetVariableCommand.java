package framework.command;

import framework.command.entity.Command;
import framework.exception.LaboratoryFrameworkException;
import framework.state.ApplicationState;
import framework.state.ApplicationStateAware;
import framework.utils.ConsoleUtils;
import framework.utils.ConverterUtils;
import framework.utils.ValidationUtils;
import framework.variable.entity.Variable;
import framework.variable.holder.VariableHolder;
import framework.variable.holder.VariableHolderAware;
import lombok.Setter;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Map;
import java.util.Objects;

@Setter
public class GetVariableCommand extends AbstractRunnableCommand
        implements ApplicationStateAware, VariableHolderAware {

    private ApplicationState applicationState;

    private VariableHolder variableHolder;

    public GetVariableCommand() {
        super("get");
    }

    @Override
    public void execute(String[] args) {
        assertFieldsArePresent();
        try {
            Map<String, String> parsedArgs = parseArgs(args);
            String variableName = parsedArgs.get("var");
            if (assertVariableIsKnown(variableName)) {
                Object value = applicationState.getVariable(variableName);
                int precision = getPrecision(parsedArgs);
                printVariable(variableName, precision, value);
            }
        } catch (LaboratoryFrameworkException ex) {
            ConsoleUtils.println(ex.getMessage());
        }
    }

    private boolean assertVariableIsKnown(String variableName) {
        if (variableName == null) {
            Command runnableCommand = commandHolder.getCommand("get");
            ConsoleUtils.println(runnableCommand.getConstraintViolationMessage());
            return false;
        }
        Variable variable = variableHolder.getVariable(variableName);
        if (variable == null) {
            ConsoleUtils.println("Unknown variable");
            return false;
        }
        return true;
    }

    private static void printVariable(String variableName, int precision, Object value) {
        ValidationUtils.requireNonNull(value);
        if (Objects.equals(value.getClass(), Interval.class)) {
            Interval interval = (Interval) value;
            ConsoleUtils.printInterval(interval);
            return;
        }
        if (value instanceof RealMatrix) {
            RealMatrix matrix = (RealMatrix) value;
            ConsoleUtils.printMatrix(precision, matrix);
            return;
        }
        if (value instanceof RealVector) {
            RealVector vector = (RealVector) value;
            ConsoleUtils.printVector(precision, vector);
            return;
        }
        ConsoleUtils.println(String.format("%s = %s", variableName, value));
    }

    private int getPrecision(Map<String, String> parsedArgs) {
        String precision = parsedArgs.get("precision");
        if (precision == null) {
            return 3;
        }
        return ConverterUtils.integerFromString(precision);
    }

    private void assertFieldsArePresent() throws LaboratoryFrameworkException {
        ValidationUtils.requireNonNull(variableHolder, "Variable holder must not be null");
        ValidationUtils.requireNonNull(applicationState, "Application state must not be null");
        ValidationUtils.requireNonNull(commandHolder, "Command holder must not be null");
    }
}
