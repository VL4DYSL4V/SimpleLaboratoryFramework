package framework.enums;

import framework.exception.LaboratoryFrameworkException;
import framework.utils.ValidationUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This enum is designed to represent string constants that correspond with keys in property file
 */
@Getter
@RequiredArgsConstructor
public enum PropertyNames {

    APPLICATION_NAME("application.name"),
    APPLICATION_AUTHOR("application.author"),
    COMMAND_PREFIX("command"),
    COMMAND_SUFFIX_NAME("name"),
    COMMAND_SUFFIX_ARITY("arity"),
    COMMAND_SUFFIX_DESCRIPTION("description"),
    COMMAND_SUFFIX_CONSTRAINT_VIOLATION_MESSAGE("constraint-violation-message");

    private final String name;

    /**
     * Method designed to extract command name from key
     *
     * @param key - key in property in format: {@link PropertyNames#COMMAND_PREFIX#name}.command_name.some-suffix.
     *            command_name must not be empty string or whitespace
     * @return command_name
     * @throws LaboratoryFrameworkException if Key does not correspond with described above format
     */
    public static String extractCommandName(String key) throws LaboratoryFrameworkException {
        ValidationUtils.requireNotEmpty(key, "Key is not specified");
        if (!key.startsWith(COMMAND_PREFIX.name) || key.length() < COMMAND_PREFIX.name.length() + 1) {
            throw new LaboratoryFrameworkException(String.format("Supplied key %s is not a command", key));
        }
        String withoutPrefix = key.substring(COMMAND_PREFIX.name.length() + 1);
        int dotIndex = withoutPrefix.indexOf('.');
        if (dotIndex == -1) {
            throw new LaboratoryFrameworkException(String.format("Invalid command: %s", key));
        }
        String result = withoutPrefix.substring(0, dotIndex);
        ValidationUtils.requireNotEmpty(result, "Command name must not be empty");
        return result;
    }

}
