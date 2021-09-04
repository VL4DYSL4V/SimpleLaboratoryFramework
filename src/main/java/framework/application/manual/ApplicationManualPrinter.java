package framework.application.manual;

import framework.enums.PropertyNames;
import framework.exception.LaboratoryFrameworkException;
import framework.command.CommandDtoHolder;
import framework.command.dto.CommandDto;
import framework.utils.ConsoleUtils;
import framework.utils.ValidationUtils;

import java.util.Map;
import java.util.Properties;

public class ApplicationManualPrinter {

    private final String manual;

    public ApplicationManualPrinter(Properties applicationProperties, CommandDtoHolder commandDtoHolder) {
        this.manual = buildManual(applicationProperties, commandDtoHolder);
    }

    public void printManual() {
        ConsoleUtils.print(this.manual);
    }

    private String buildManual(Properties applicationProperties, CommandDtoHolder commandDtoHolder) {
        StringBuilder stringBuilder = new StringBuilder();
        appendApplicationPart(stringBuilder, applicationProperties);
        appendCommandsPart(stringBuilder, commandDtoHolder);
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

        String applicationDescription = applicationProperties.getProperty(PropertyNames.APPLICATION_DESCRIPTION.getName());
        ValidationUtils.requireNotEmpty(applicationDescription, "Application description is not specified");
        destination.append(String.format("Application description: %s%n", applicationDescription));
    }

    /**
     * Appends 'commands' part and adds line separator character to the end
     */
    private void appendCommandsPart(StringBuilder destination, CommandDtoHolder commandDtoHolder) {
        Map<String, CommandDto> commands = commandDtoHolder.getCommandDtos();
        destination.append(String.format("Commands:%n"));
        commands.forEach((k, v) -> {
            destination.append(String.format("* %s:%n", k));
            destination.append(String.format("\tDescription: %s%n", v.getDescription()));
            destination.append(String.format("\tArity: %s%n", v.getArity()));
        });
    }
}
