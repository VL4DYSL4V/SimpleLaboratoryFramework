package framework.enums;

import framework.exception.LaboratoryFrameworkException;
import framework.utils.ValidationUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PropertyNames {

    APPLICATION_NAME("application.name"),
    APPLICATION_AUTHOR("application.name"),
    OPTION_PREFIX("option"),
    OPTION_SUFFIX_NAME("name"),
    OPTION_SUFFIX_ARITY("arity"),
    OPTION_SUFFIX_DESCRIPTION("description"),
    OPTION_SUFFIX_CONSTRAINT_VIOLATION_MESSAGE("constraint-violation-message");

    private final String name;

    public static String extractOptionName(String key) throws LaboratoryFrameworkException {
        ValidationUtils.requireNotEmpty(key, "Key is not specified");
        if (!key.startsWith(OPTION_PREFIX.name) || key.length() < OPTION_PREFIX.name.length() + 1) {
            throw new LaboratoryFrameworkException(String.format("Supplied key %s is not an option", key));
        }
        String withoutPrefix = key.substring(OPTION_PREFIX.name.length() + 1);
        int dotIndex = withoutPrefix.indexOf('.');
        if(dotIndex == -1) {
            throw new LaboratoryFrameworkException(String.format("Invalid option: %s", key));
        }
        String result = withoutPrefix.substring(0, dotIndex);
        ValidationUtils.requireNotEmpty(result, "Option name must not be empty");
        return result;
    }

}
