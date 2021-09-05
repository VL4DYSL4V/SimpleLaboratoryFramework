package framework.application.info;

import framework.command.holder.CommandHolder;
import framework.command.entity.Command;
import framework.enums.PropertyName;
import framework.exception.LaboratoryFrameworkException;
import framework.utils.ConsoleUtils;
import framework.utils.ValidationUtils;
import framework.variable.holder.VariableHolder;
import framework.variable.entity.Variable;

import java.util.Map;
import java.util.Properties;

/**
 * Class is created to build and print greeting and manual
 */
public class ApplicationInfoPrinter {

    private final String greeting;

    private final String manual;

    public ApplicationInfoPrinter(Properties applicationProperties, CommandHolder commandHolder, VariableHolder variableHolder) {
        this.greeting = buildGreeting(applicationProperties);
        this.manual = buildManual(commandHolder, variableHolder);
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


    private String buildManual(CommandHolder commandHolder, VariableHolder variableHolder) {
        StringBuilder stringBuilder = new StringBuilder();
        appendVariablesPart(stringBuilder, variableHolder);
        appendCommandsPart(stringBuilder, commandHolder);
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
    private void appendVariablesPart(StringBuilder destination, VariableHolder variableHolder) {
        Map<String, Variable> variables = variableHolder.getVariableDtos();
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
    private void appendCommandsPart(StringBuilder destination, CommandHolder commandHolder) {
        Map<String, Command> commands = commandHolder.getCommands();
        destination.append(String.format("Commands:%n"));
        commands.forEach((k, v) -> {
            destination.append(String.format("* %s:%n", k));
            destination.append(String.format("\tDescription: %s%n", v.getDescription()));
            destination.append(String.format("\tArity: %s%n", v.getArity()));
        });
        destination.append(System.lineSeparator());
    }
}
