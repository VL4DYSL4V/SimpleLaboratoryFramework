package framework.command.parser;

import framework.exception.LaboratoryFrameworkException;
import framework.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ArgsParser {

    private static final Pattern ARG_REGEX = Pattern.compile("--(\\S)+=(\\S)+");

    private ArgsParser() {}

    public static Map<String, String> parseArgs(String[] args) throws LaboratoryFrameworkException {
        Map<String, String> out = new HashMap<>();
        for (String arg: args) {
            if (!ARG_REGEX.matcher(arg).matches()) {
                throw new LaboratoryFrameworkException(String.format("Invalid input: %s", arg));
            }
            String[] splitBeEquals = arg.split("=", 2);
            String paramNameWithHyphens = splitBeEquals[0];
            String paramName = paramNameWithHyphens.substring(2);
            out.put(paramName, splitBeEquals[1]);
        }
        return out;
    }

}
