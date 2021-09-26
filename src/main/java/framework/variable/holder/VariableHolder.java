package framework.variable.holder;

import framework.enums.PropertyName;
import framework.enums.VariableType;
import framework.exception.LaboratoryFrameworkException;
import framework.utils.ValidationUtils;
import framework.variable.entity.MatrixVariable;
import framework.variable.entity.PolynomialFunctionVariable;
import framework.variable.entity.Variable;
import framework.variable.entity.VectorVariable;
import lombok.Data;
import lombok.Getter;

import javax.annotation.concurrent.ThreadSafe;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
@ThreadSafe
public class VariableHolder {

    private final Map<String, Variable> variables;

    public VariableHolder(Properties applicationProperties) {
        Map<String, List<String>> variableNameToListOfKeysForIt =
                applicationProperties.stringPropertyNames()
                        .stream()
                        .filter(key -> key.startsWith(PropertyName.VARIABLE_PREFIX.getName()))
                        .collect(Collectors.groupingBy(PropertyName::extractVariableName));
        this.variables = fromVariableNameToListOfKeysForIt(variableNameToListOfKeysForIt, applicationProperties);
    }

    public Variable getVariable(String variableName) {
        return variables.get(variableName);
    }

    /**
     * Method builds map of variable name to variable dto. It returns unmodifiable and threadsafe map
     */
    private Map<String, Variable> fromVariableNameToListOfKeysForIt(
            Map<String, List<String>> variableNameToListOfKeysForIt,
            Properties applicationProperties) {
        Map<String, MutableVariableDto> container = new HashMap<>();
        for (String variableName : variableNameToListOfKeysForIt.keySet()) {
            final List<String> listOfKeys = variableNameToListOfKeysForIt.get(variableName);
            MutableVariableDto dto = new MutableVariableDto();
            container.putIfAbsent(variableName, dto);
            for (String variableKey : listOfKeys) {
                String value = applicationProperties.getProperty(variableKey);
                setParamToMutableDto(dto, variableKey, value);
            }
        }
        Map<String, Variable> out = new ConcurrentHashMap<>();
        container.forEach((key, value) -> out.put(key, mapMutableVariableDtoToVariableDto(value)));
        return Collections.unmodifiableMap(out);
    }

    private Variable mapMutableVariableDtoToVariableDto(MutableVariableDto dto) {
        if (dto.getType() == null) {
            throw new IllegalStateException("Type must be specified for all variables");
        }
        switch (dto.getType()) {
            case VECTOR:
                if (dto.getVectorLength() < 1) {
                    throw new IllegalArgumentException("Vector length must be >= 1");
                }
                return new VectorVariable(dto.getName(),
                        dto.getType(),
                        dto.getDescription(),
                        dto.isCannotBeSetFromInput(),
                        dto.getConstraintViolationMessage(),
                        dto.getVectorLength());
            case MATRIX:
                if (dto.getMatrixRowCount() < 1 || dto.getMatrixColumnCount() < 1) {
                    throw new IllegalArgumentException("Both matrix row count and column count must be >= 1");
                }
                return new MatrixVariable(dto.getName(),
                        dto.getType(),
                        dto.getDescription(),
                        dto.isCannotBeSetFromInput(),
                        dto.getConstraintViolationMessage(),
                        dto.getMatrixRowCount(),
                        dto.getMatrixColumnCount());
            case POLYNOMIAL_FUNCTION:
                if (dto.getMatrixRowCount() < 0) {
                    throw new IllegalArgumentException("Max polynomial degree must be >= 0");
                }
                return new PolynomialFunctionVariable(
                        dto.getName(),
                        dto.getType(),
                        dto.getDescription(),
                        dto.isCannotBeSetFromInput(),
                        dto.getConstraintViolationMessage(),
                        dto.getMaxPolynomialDegree()
                );
        }
        return new Variable(
                dto.getName(),
                dto.getType(),
                dto.getDescription(),
                dto.isCannotBeSetFromInput(),
                dto.getConstraintViolationMessage());
    }

    /**
     * Method sets value to dto's field that corresponds with variableKey
     *
     * @param dto      - dto which will be mutated by this method
     * @param variable - variable key in property file
     * @param value    - value for variableKey
     * @throws LaboratoryFrameworkException if none of fields are corresponding with supplied key
     *                                      or if value violates constraints
     */
    private void setParamToMutableDto(MutableVariableDto dto, String variable, String value)
            throws LaboratoryFrameworkException {
        if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_NAME.getName())) {
            ValidationUtils.requireNotEmpty(value, "Name must be specified");
            dto.setName(value);
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_DESCRIPTION.getName())) {
            ValidationUtils.requireNotEmpty(value, "Description must be provided");
            dto.setDescription(value);
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_CONSTRAINT_VIOLATION_MESSAGE.getName())) {
            dto.setConstraintViolationMessage(value);
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_TYPE.getName())) {
            try {
                final VariableType type = VariableType.valueOf(value.toUpperCase(Locale.ROOT));
                dto.setType(type);
            } catch (Throwable e) {
                throw new LaboratoryFrameworkException(String.format("Unknown type: %s", value));
            }
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_CANNOT_BE_SET_FROM_INPUT.getName())) {
            dto.setCannotBeSetFromInput(Boolean.parseBoolean(value));
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_VECTOR_LENGTH.getName())) {
            dto.setVectorLength(Integer.parseInt(value));
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_POLYNOMIAL_MAX_DEGREE.getName())) {
            dto.setMaxPolynomialDegree(Integer.parseInt(value));
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_MATRIX_ROW_COUNT.getName())) {
            dto.setMatrixRowCount(Integer.parseInt(value));
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_MATRIX_COLUMN_COUNT.getName())) {
            dto.setMatrixColumnCount(Integer.parseInt(value));
        } else {
            throw new LaboratoryFrameworkException(String.format("Unknown key: %s", variable));
        }
    }


    @Data
    private static class MutableVariableDto {

        private String name;

        private VariableType type;

        private String description;

        private boolean cannotBeSetFromInput;

        private String constraintViolationMessage;

        private int vectorLength;

        private int matrixRowCount;

        private int matrixColumnCount;

        private int maxPolynomialDegree;

    }

}
