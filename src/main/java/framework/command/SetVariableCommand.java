package framework.command;

import framework.command.entity.Command;
import framework.enums.VariableType;
import framework.exception.LaboratoryFrameworkException;
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
import java.util.Optional;
import java.util.function.Supplier;

@Setter
public class SetVariableCommand extends AbstractRunnableCommand
        implements VariableHolderAware {

    private VariableHolder variableHolder;

    private final Map<VariableType, Supplier<Object>> variableTypeToValueSupplierMap =
            new EnumMap<>(VariableType.class);

    public SetVariableCommand() {
        super("set");
        setValueSuppliers();
    }

    @Override
    public void execute(String[] args) {
        assertFieldsArePresent();
        Optional<String> variableNameHolder = getVariableNameFromArgs(args);
        if (variableNameHolder.isEmpty()) {
            Command runnableCommand = commandHolder.getCommand("set");
            ConsoleUtils.println(runnableCommand.getConstraintViolationMessage());
            return;
        }
        Variable variable = variableHolder.getVariable(variableNameHolder.get());
        if (variable == null) {
            ConsoleUtils.println("Unknown variable");
            return;
        }
        if (variable.isCannotBeSetFromInput() || variable.getType() == VariableType.OBJECT) {
            ConsoleUtils.println("You cannot set this variable from input");
            return;
        }
        Object result = getValueForValue(variable);
        applicationState.setVariable(variableNameHolder.get(), result);
    }

    private Optional<String> getVariableNameFromArgs(String[] args) {
        Map<String, String> parsedArgs;
        try {
            parsedArgs = parseArgs(args);
        } catch (LaboratoryFrameworkException ex) {
            ConsoleUtils.println(ex.getMessage());
            return Optional.empty();
        }
        Command runnableCommand = commandHolder.getCommand("set");
        String paramName = runnableCommand.getOptions().iterator().next();
        String variableName = parsedArgs.get(paramName);
        return Optional.ofNullable(variableName);
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
