package framework.application.info;

import framework.command.CommandDtoHolder;
import framework.command.dto.CommandDto;
import framework.enums.PropertyName;
import framework.exception.LaboratoryFrameworkException;
import framework.utils.ConsoleUtils;
import framework.utils.ValidationUtils;
import framework.variable.VariableDtoHolder;
import framework.variable.dto.VariableDto;

import java.util.Map;
import java.util.Properties;

public class ApplicationInfoPrinter {

    private final String greeting;

    private final String manual;

    public ApplicationInfoPrinter(Properties applicationProperties, CommandDtoHolder commandDtoHolder, VariableDtoHolder variableDtoHolder) {
        this.greeting = buildGreeting(applicationProperties);
        this.manual = buildManual(commandDtoHolder, variableDtoHolder);
    }

    public void printManual() {
        ConsoleUtils.println(this.manual);
    }

    public void printGreeting() {
        ConsoleUtils.println(this.greeting);
    }

    private String buildGreeting(Properties applicationProperties) {
        StringBuilder stringBuilder = new StringBuilder();
        appendApplicationPart(stringBuilder, applicationProperties);
        return stringBuilder.toString();
    }


    private String buildManual(CommandDtoHolder commandDtoHolder, VariableDtoHolder variableDtoHolder) {
        StringBuilder stringBuilder = new StringBuilder();
        appendVariablesPart(stringBuilder, variableDtoHolder);
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
        String applicationName = applicationProperties.getProperty(PropertyName.APPLICATION_NAME.getName());
        ValidationUtils.requireNotEmpty(applicationName, "Application name is not specified");
        destination.append(String.format("Application name: %s%n", applicationName));

        String applicationAuthor = applicationProperties.getProperty(PropertyName.APPLICATION_AUTHOR.getName());
        ValidationUtils.requireNotEmpty(applicationAuthor, "Application author is not specified");
        destination.append(String.format("Application author: %s%n", applicationAuthor));

        String applicationDescription = applicationProperties.getProperty(PropertyName.APPLICATION_DESCRIPTION.getName());
        ValidationUtils.requireNotEmpty(applicationDescription, "Application description is not specified");
        destination.append(String.format("Application description: %s%n", applicationDescription));
    }

    /**
     * Appends 'variables' part and adds line separator character to the end
     */
    private void appendVariablesPart(StringBuilder destination, VariableDtoHolder variableDtoHolder) {
        Map<String, VariableDto> variables = variableDtoHolder.getVariableDtos();
        destination.append(String.format("Variables:%n"));
        variables.forEach((k, v) -> {
            destination.append(String.format("* %s:%n", k));
            destination.append(String.format("\tDescription: %s%n", v.getDescription()));
            destination.append(String.format("\tType: %s%n", v.getType()));
        });
        destination.append(System.lineSeparator());
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
        destination.append(System.lineSeparator());
    }
}
