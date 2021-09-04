package framework.variable;

import framework.enums.PropertyName;
import framework.enums.VariableType;
import framework.exception.LaboratoryFrameworkException;
import framework.utils.ValidationUtils;
import framework.variable.dto.VariableDto;
import lombok.Data;
import lombok.Getter;

import javax.annotation.concurrent.ThreadSafe;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
@ThreadSafe
public class VariableDtoHolder {

    private final Map<String, VariableDto> variableDtos;

    public VariableDtoHolder(Properties applicationProperties) {
        Map<String, List<String>> variableNameToListOfKeysForIt =
                applicationProperties.stringPropertyNames()
                        .stream()
                        .filter(key -> key.startsWith(PropertyName.VARIABLE_PREFIX.getName()))
                        .collect(Collectors.groupingBy(PropertyName::extractVariableName));
        this.variableDtos = fromVariableNameToListOfKeysForIt(variableNameToListOfKeysForIt, applicationProperties);
    }

    public VariableDto getVariableDto(String variableName) {
        return variableDtos.get(variableName);
    }

    /**
     * Method builds map of variable name to variable dto. It returns unmodifiable and threadsafe map
     */
    private Map<String, VariableDto> fromVariableNameToListOfKeysForIt(
            Map<String, List<String>> variableNameToListOfKeysForIt,
            Properties applicationProperties) {
        Map<String, MutableVariableDto> container = new HashMap<>();
        for (String variableName : variableNameToListOfKeysForIt.keySet()) {
            MutableVariableDto dto = new MutableVariableDto();
            container.putIfAbsent(variableName, dto);
            for (String variableKey : variableNameToListOfKeysForIt.get(variableName)) {
                String value = applicationProperties.getProperty(variableKey);
                setParamToMutableDto(dto, variableKey, value);
            }
        }
        Map<String, VariableDto> out = new ConcurrentHashMap<>();
        container.forEach((key, value) -> out.put(key, mapMutableVariableDtoToVariableDto(value)));
        return Collections.unmodifiableMap(out);
    }

    private VariableDto mapMutableVariableDtoToVariableDto(MutableVariableDto dto) {
        return new VariableDto(
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
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_TYPE.getName())) {
            try {
                final VariableType type = VariableType.valueOf(value.toUpperCase(Locale.ROOT));
                dto.setType(type);
            } catch (Throwable e) {
                throw new LaboratoryFrameworkException(String.format("Unknown type: %s", value));
            }
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_DESCRIPTION.getName())) {
            ValidationUtils.requireNotEmpty(value, "Description must be provided");
            dto.setDescription(value);
        } else if (variable.endsWith(PropertyName.VARIABLE_SUFFIX_CONSTRAINT_VIOLATION_MESSAGE.getName())) {
            dto.setConstraintViolationMessage(value);
        } else {
            throw new LaboratoryFrameworkException(String.format("Unknown key: %s", variable));
        }
    }

    @Data
    private static final class MutableVariableDto {

        private String name;

        private VariableType type;

        private String description;

        private String constraintViolationMessage;

    }
}
