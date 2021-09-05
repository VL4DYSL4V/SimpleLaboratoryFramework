package framework.command.holder;

import framework.command.entity.Command;
import framework.enums.PropertyName;
import framework.exception.LaboratoryFrameworkException;
import framework.utils.ValidationUtils;
import lombok.Data;
import lombok.Getter;

import javax.annotation.concurrent.ThreadSafe;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Class holds a map of command names to {@link Command}
 */
@ThreadSafe
@Getter
public final class CommandHolder {

    private final Map<String, Command> commandDtos;

    public CommandHolder(Properties applicationProperties) {
        Map<String, List<String>> commandNameToListOfKeysForIt =
                applicationProperties.stringPropertyNames()
                        .stream()
                        .filter(key -> key.startsWith(PropertyName.COMMAND_PREFIX.getName()))
                        .collect(Collectors.groupingBy(PropertyName::extractCommandName));
        this.commandDtos = fromCommandNameToListOfKeysForIt(commandNameToListOfKeysForIt, applicationProperties);
    }

    /**
     * Method builds map of command name to command dto. It returns unmodifiable and threadsafe map
     */
    private Map<String, Command> fromCommandNameToListOfKeysForIt(
            Map<String, List<String>> commandNameToListOfKeysForIt,
            Properties applicationProperties) {
        Map<String, MutableCommandDto> container = new HashMap<>();
        for (String commandName : commandNameToListOfKeysForIt.keySet()) {
            MutableCommandDto dto = new MutableCommandDto();
            container.putIfAbsent(commandName, dto);
            for (String commandKey : commandNameToListOfKeysForIt.get(commandName)) {
                String value = applicationProperties.getProperty(commandKey);
                setParamToMutableDto(dto, commandKey, value);
            }
        }
        Map<String, Command> out = new ConcurrentHashMap<>();
        container.forEach((key, value) -> out.put(key, mapMutableCommandDtoToCommandDto(value)));
        return Collections.unmodifiableMap(out);
    }

    private Command mapMutableCommandDtoToCommandDto(MutableCommandDto dto) {
        return new Command(
                dto.getName(),
                dto.getArity(),
                dto.getDescription(),
                dto.getConstraintViolationMessage());
    }

    /**
     * Method sets value to dto's field that corresponds with commandKey
     *
     * @param dto        - dto which will be mutated by this method
     * @param commandKey - command key in property file
     * @param value      - value for commandKey
     * @throws LaboratoryFrameworkException if none of fields are corresponding with supplied key
     *                                      or if value violates constraints
     */
    private void setParamToMutableDto(MutableCommandDto dto, String commandKey, String value)
            throws LaboratoryFrameworkException {
        if (commandKey.endsWith(PropertyName.COMMAND_SUFFIX_NAME.getName())) {
            ValidationUtils.requireNotEmpty(value, "Name must be specified");
            dto.setName(value);
        } else if (commandKey.endsWith(PropertyName.COMMAND_SUFFIX_ARITY.getName())) {
            int arity = Integer.parseInt(value);
            ValidationUtils.requireGreaterOrEqualThanZero(arity, "Arity must be >= 0");
            dto.setArity(Integer.parseInt(value));
        } else if (commandKey.endsWith(PropertyName.COMMAND_SUFFIX_DESCRIPTION.getName())) {
            ValidationUtils.requireNotEmpty(value, "Description must be provided");
            dto.setDescription(value);
        } else if (commandKey.endsWith(PropertyName.COMMAND_SUFFIX_CONSTRAINT_VIOLATION_MESSAGE.getName())) {
            dto.setConstraintViolationMessage(value);
        } else {
            throw new LaboratoryFrameworkException(String.format("Unknown key: %s", commandKey));
        }
    }

    @Data
    private static final class MutableCommandDto {

        private String name;

        private int arity;

        private String description;

        private String constraintViolationMessage;

    }

}
