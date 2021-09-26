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
import framework.variable.entity.MatrixVariable;
import framework.variable.entity.PolynomialFunctionVariable;
import framework.variable.entity.Variable;
import framework.variable.entity.VectorVariable;
import framework.variable.holder.VariableHolder;
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
        Variable variable = variableHolder.getVariable(variableName);
        if (variable == null) {
            ConsoleUtils.println("Unknown variable");
            return;
        }
        if (variable.isCannotBeSetFromInput()) {
            ConsoleUtils.println("You cannot set this variable from input");
            return;
        }
        Object result = getValueForValue(variable);
        applicationState.setVariable(variableName, result);
    }

    private Object getValueForValue(Variable variable) {
        switch (variable.getType()) {
            case VECTOR:
                VectorVariable vectorVariable = (VectorVariable) variable;
                return ConsoleUtils.askForVectorRepeatedly(vectorVariable.getLength());
            case MATRIX:
                MatrixVariable matrixVariable = (MatrixVariable) variable;
                return ConsoleUtils.askForMatrixRepeatedly(matrixVariable.getRowCount(), matrixVariable.getColumnCount());
            case POLYNOMIAL_FUNCTION:
                PolynomialFunctionVariable polynomialFunction = (PolynomialFunctionVariable) variable;
                return ConsoleUtils.askForPolynomialFunctionRepeatedly(polynomialFunction.getMaxDegree());
        }
        return variableTypeToValueSupplierMap.get(variable.getType()).get();
    }

    private void setValueSuppliers() {
        variableTypeToValueSupplierMap.put(VariableType.STRING, ConsoleUtils::askForStringRepeatedly);
        variableTypeToValueSupplierMap.put(VariableType.BIG_DECIMAL, ConsoleUtils::askForBigDecimalRepeatedly);
        variableTypeToValueSupplierMap.put(VariableType.BIG_INTEGER, ConsoleUtils::askForBigBigIntegerRepeatedly);
        variableTypeToValueSupplierMap.put(VariableType.BYTE, ConsoleUtils::askForByteRepeatedly);
        variableTypeToValueSupplierMap.put(VariableType.SHORT, ConsoleUtils::askForShortRepeatedly);
        variableTypeToValueSupplierMap.put(VariableType.INTEGER, ConsoleUtils::askForIntegerRepeatedly);
        variableTypeToValueSupplierMap.put(VariableType.LONG, ConsoleUtils::askForLongRepeatedly);
        variableTypeToValueSupplierMap.put(VariableType.BOOLEAN, ConsoleUtils::askForBooleanRepeatedly);
        variableTypeToValueSupplierMap.put(VariableType.CHARACTER, ConsoleUtils::askForCharacterRepeatedly);
        variableTypeToValueSupplierMap.put(VariableType.FLOAT, ConsoleUtils::askForFloatRepeatedly);
        variableTypeToValueSupplierMap.put(VariableType.DOUBLE, ConsoleUtils::askForDoubleRepeatedly);
        variableTypeToValueSupplierMap.put(VariableType.COMPLEX_NUMBER, ConsoleUtils::askForComplexNumberRepeatedly);
        variableTypeToValueSupplierMap.put(VariableType.INTERVAL, ConsoleUtils::askForIntervalRepeatedly);
    }

    private void assertFieldsArePresent() throws LaboratoryFrameworkException {
        ValidationUtils.requireNonNull(variableHolder, "Variable holder must not be null");
        ValidationUtils.requireNonNull(applicationState, "Application state must not be null");
        ValidationUtils.requireNonNull(commandHolder, "Command holder must not be null");
    }
}
