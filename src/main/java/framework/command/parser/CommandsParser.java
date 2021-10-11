package framework.command.parser;

import framework.command.entity.Command;
import framework.enums.PropertyName;
import framework.exception.LaboratoryFrameworkException;
import framework.utils.ValidationUtils;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

public class CommandsParser {

    private CommandsParser(){}

    public static Map<String, Command> getCommandNameToCommand(Properties applicationProperties) {
        Map<String, List<String>> commandNameToListOfKeysForIt =
                applicationProperties.stringPropertyNames()
                        .stream()
                        .filter(key -> key.startsWith(PropertyName.COMMAND_PREFIX.getName()))
                        .collect(Collectors.groupingBy(PropertyName::extractCommandName));
        return fromCommandNameToListOfKeysForIt(commandNameToListOfKeysForIt, applicationProperties);
    }

    /**
     * Method builds map of command name to command dto.
     */
    private static Map<String, Command> fromCommandNameToListOfKeysForIt(
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
        Map<String, Command> out = new HashMap<>();
        container.forEach((key, value) -> out.put(key, mapMutableCommandDtoToCommandDto(value)));
        return out;
    }

    private static Command mapMutableCommandDtoToCommandDto(MutableCommandDto dto) {
        return new Command(
                dto.getName(),
                dto.getOptions(),
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
    private static void setParamToMutableDto(MutableCommandDto dto, String commandKey, String value)
            throws LaboratoryFrameworkException {
        if (commandKey.endsWith(PropertyName.COMMAND_SUFFIX_NAME.getName())) {
            ValidationUtils.requireNotEmpty(value, "Name must be specified");
            dto.setName(value);
        } else if (commandKey.endsWith(PropertyName.COMMAND_SUFFIX_OPTIONS.getName())) {
            Set<String> options = Arrays.stream(value.split(",")).collect(Collectors.toSet());
            dto.setOptions(options);
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

        private Set<String> options = new HashSet<>();

        private String description = "";

        private String constraintViolationMessage = "";

    }

}
