package framework.command.parser;

import framework.exception.LaboratoryFrameworkException;
import framework.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Map;

public class ArgsParser {

    private ArgsParser() {}

    public static Map<String, String> parseArgs(String[] args) throws LaboratoryFrameworkException {
        Map<String, String> out = new HashMap<>();
        for (String arg: args) {
            String[] splitBeEquals = arg.split("=");
            ValidationUtils.requireGreaterOrEqualThan(splitBeEquals.length, 1,
                    "At least one '=' must appear");
            String paramNameWithHyphens = splitBeEquals[0];
            ValidationUtils.requireTrue(paramNameWithHyphens.startsWith("--"),
                    String.format("Parameter must start with '--', got: %s", paramNameWithHyphens));
            String paramName = paramNameWithHyphens.substring(2);
            ValidationUtils.requireNotEmpty(paramName, "Parameter must not be empty string");

            ValidationUtils.requireNotEmpty(splitBeEquals[1], "Value must not be empty");

            out.put(paramName, splitBeEquals[1]);
        }
        return out;
    }

}
