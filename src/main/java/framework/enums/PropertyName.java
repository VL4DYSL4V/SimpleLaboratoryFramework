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
public enum PropertyName {

    APPLICATION_NAME("application.name"),
    APPLICATION_AUTHOR("application.author"),
    APPLICATION_DESCRIPTION("application.description"),
    COMMAND_PREFIX("command"),
    COMMAND_SUFFIX_NAME("name"),
    COMMAND_SUFFIX_OPTIONS("options"),
    COMMAND_SUFFIX_DESCRIPTION("description"),
    COMMAND_SUFFIX_CONSTRAINT_VIOLATION_MESSAGE("constraint-violation-message"),

    VARIABLE_PREFIX("variable"),
    VARIABLE_SUFFIX_NAME("name"),
    VARIABLE_SUFFIX_TYPE("type"),
    VARIABLE_SUFFIX_CANNOT_BE_SET_FROM_INPUT("cannot-be-set-from-input"),
    VARIABLE_SUFFIX_DESCRIPTION("description"),
    VARIABLE_SUFFIX_VECTOR_LENGTH("vector-length"),
    VARIABLE_SUFFIX_POLYNOMIAL_MAX_DEGREE("polynomial-max-degree"),
    VARIABLE_SUFFIX_MATRIX_ROW_COUNT("matrix-row-count"),
    VARIABLE_SUFFIX_MATRIX_COLUMN_COUNT("matrix-column-count"),
    VARIABLE_SUFFIX_CONSTRAINT_VIOLATION_MESSAGE("constraint-violation-message");

    private final String name;

    /**
     * Method designed to extract command name from key
     *
     * @param key - key in property in format: {@link PropertyName#COMMAND_PREFIX#name}.command_name.some-suffix.
     *            command_name must not be empty string or whitespace
     * @return command_name
     * @throws LaboratoryFrameworkException if key does not correspond with described above format
     */
    public static String extractCommandName(String key) throws LaboratoryFrameworkException {
        return extractName(key, PropertyName.COMMAND_PREFIX);
    }

    /**
     * Method designed to extract variable name from key
     *
     * @param key - key in property in format: {@link PropertyName#VARIABLE_PREFIX#name}.variable_name.some-suffix.
     *            variable_name must not be empty string or whitespace
     * @return variable_name
     * @throws LaboratoryFrameworkException if key does not correspond with described above format
     */
    public static String extractVariableName(String key) throws LaboratoryFrameworkException {
        return extractName(key, PropertyName.VARIABLE_PREFIX);
    }

    /**
     * Method designed to extract a name from key
     *
     * @param key - key in property in format: keyType.the_name.some-suffix.
     *            the_name must not be empty string or whitespace
     * @return the_name
     * @throws LaboratoryFrameworkException if key does not correspond with described above format
     */
    private static String extractName(String key, PropertyName propertyName) throws LaboratoryFrameworkException {
        ValidationUtils.requireNotEmpty(key, "Key is not specified");
        if (!key.startsWith(propertyName.name) || key.length() < propertyName.name.length() + 1) {
            throw new LaboratoryFrameworkException(String.format("Supplied key %s is not a command", key));
        }
        String withoutPrefix = key.substring(propertyName.name.length() + 1);
        int dotIndex = withoutPrefix.indexOf('.');
        if (dotIndex == -1) {
            throw new LaboratoryFrameworkException(String.format("Invalid command: %s", key));
        }
        String result = withoutPrefix.substring(0, dotIndex);
        ValidationUtils.requireNotEmpty(result, "Command name must not be empty");
        return result;
    }

}
