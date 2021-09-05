package framework.variable.holder;

import framework.enums.PropertyName;
import framework.enums.VariableType;
import framework.exception.LaboratoryFrameworkException;
import framework.utils.ConverterUtils;
import framework.utils.ValidationUtils;
import framework.variable.entity.MatrixVariable;
import framework.variable.entity.Variable;
import framework.variable.entity.VectorVariable;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
            MutableVariableDto dto = createWithCorrectType(listOfKeys, applicationProperties);
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

    private MutableVariableDto createWithCorrectType(List<String> keysForVariable, Properties applicationProperties) {
        for (String key : keysForVariable) {
            String value = applicationProperties.getProperty(key);
            ValidationUtils.requireNonNull(value);
            ValidationUtils.requireNonNull(key);
            if (key.endsWith(PropertyName.VARIABLE_SUFFIX_TYPE.getName())) {
                try {
                    final VariableType type = VariableType.valueOf(value.toUpperCase(Locale.ROOT));
                    return createForType(type);
                } catch (Throwable e) {
                    throw new LaboratoryFrameworkException(String.format("Unknown type: %s", value));
                }
            }
        }
        throw new LaboratoryFrameworkException("Property type must be specified");
    }

    private MutableVariableDto createForType(VariableType type) {
        MutableVariableDto mutableVariableDto;
        switch (type) {
            case VECTOR:
                mutableVariableDto = new MutableVectorVariableDto();
                break;
            case MATRIX:
                mutableVariableDto = new MutableMatrixVariableDto();
                break;
            default:
                mutableVariableDto = new MutableVariableDto();
                break;
        }
        mutableVariableDto.setType(type);
        return mutableVariableDto;
    }

    private Variable mapMutableVariableDtoToVariableDto(MutableVariableDto dto) {
        switch (dto.getType()) {
            case VECTOR:
                return new VectorVariable(dto.getName(),
                        dto.getType(),
                        dto.getDescription(),
                        dto.getConstraintViolationMessage(),
                        ((MutableVectorVariableDto) dto).getLength());
            case MATRIX:
                return new MatrixVariable(dto.getName(),
                        dto.getType(),
                        dto.getDescription(),
                        dto.getConstraintViolationMessage(),
                        ((MutableMatrixVariableDto) dto).getRowCount(),
                        ((MutableMatrixVariableDto) dto).getColumnCount());
        }
        return new Variable(
                dto.getName(),
                dto.getType(),
                dto.getDescription(),
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
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_VECTOR_LENGTH.getName())) {
            checkIfMutableVector(dto);
            ((MutableVectorVariableDto) dto).setLength(ConverterUtils.integerFromString(value));
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_MATRIX_ROW_COUNT.getName())) {
            checkIfMutableMatrix(dto);
            ((MutableMatrixVariableDto) dto).setRowCount(ConverterUtils.integerFromString(value));
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_MATRIX_COLUMN_COUNT.getName())) {
            checkIfMutableMatrix(dto);
            ((MutableMatrixVariableDto) dto).setColumnCount(ConverterUtils.integerFromString(value));
        } else {
            throw new LaboratoryFrameworkException(String.format("Unknown key: %s", variable));
        }
    }

    private void checkIfMutableVector(MutableVariableDto dto) {
        if (dto.getType() != VariableType.VECTOR || !Objects.equals(dto.getClass(), MutableVectorVariableDto.class)) {
            throw new LaboratoryFrameworkException("Supplied value is not mutable vector");
        }
    }

    private void checkIfMutableMatrix(MutableVariableDto dto) {
        if (dto.getType() != VariableType.MATRIX || !Objects.equals(dto.getClass(), MutableMatrixVariableDto.class)) {
            throw new LaboratoryFrameworkException("Supplied value is not mutable vector");
        }
    }

    @Data
    private static class MutableVariableDto {

        private String name;

        private VariableType type;

        private String description;

        private String constraintViolationMessage;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    private static class MutableVectorVariableDto extends MutableVariableDto {

        private int length;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    private static class MutableMatrixVariableDto extends MutableVariableDto {

        private int rowCount;

        private int columnCount;

    }
}
