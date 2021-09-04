package framework.application.manual;

import framework.enums.PropertyNames;
import framework.exception.LaboratoryFrameworkException;
import framework.option.OptionHolder;
import framework.option.dto.OptionDto;
import framework.utils.ConsoleUtils;
import framework.utils.ValidationUtils;

import java.util.Map;
import java.util.Properties;

public class ApplicationManualPrinter {

    private final String manual;

    public ApplicationManualPrinter(Properties applicationProperties, OptionHolder optionHolder) {
        this.manual = buildManual(applicationProperties, optionHolder);
    }

    public void printManual() {
        ConsoleUtils.print(this.manual);
    }

    private String buildManual(Properties applicationProperties, OptionHolder optionHolder) {
        StringBuilder stringBuilder = new StringBuilder();
        appendApplicationPart(stringBuilder, applicationProperties);
        appendOptionsPart(stringBuilder, optionHolder);
        return stringBuilder.toString();
    }

    /**
     * Appends 'application' part and adds line separator character to the end
     *
     * @throws LaboratoryFrameworkException if any parameter in properties is null or empty
     */
    private void appendApplicationPart(StringBuilder destination, Properties applicationProperties)
            throws LaboratoryFrameworkException {
        String applicationName = applicationProperties.getProperty(PropertyNames.APPLICATION_NAME.getName());
        ValidationUtils.requireNotEmpty(applicationName, "Application name is not specified");
        destination.append(String.format("Application name: %s%n", applicationName));

        String applicationAuthor = applicationProperties.getProperty(PropertyNames.APPLICATION_AUTHOR.getName());
        ValidationUtils.requireNotEmpty(applicationAuthor, "Application author is not specified");
        destination.append(String.format("Application author: %s%n", applicationAuthor));
    }

    /**
     * Appends 'options' part and adds line separator character to the end
     *
     * @throws LaboratoryFrameworkException if any parameter in properties is null or empty
     */
    private void appendOptionsPart(StringBuilder destination, OptionHolder optionHolder)
            throws LaboratoryFrameworkException {
        Map<String, OptionDto> options = optionHolder.getOptionDtos();
        destination.append(String.format("Options:%n"));
        options.forEach((k, v) -> {
            destination.append(String.format("* %s:%n", k));
            destination.append(String.format("\tDescription: %s%n", v.getDescription()));
            destination.append(String.format("\tArity: %s%n", v.getArity()));
        });
    }
}
