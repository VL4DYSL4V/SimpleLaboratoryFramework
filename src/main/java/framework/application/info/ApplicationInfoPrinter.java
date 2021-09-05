package framework.application.info;

import framework.command.holder.CommandHolder;
import framework.command.entity.Command;
import framework.enums.PropertyName;
import framework.enums.VariableType;
import framework.exception.LaboratoryFrameworkException;
import framework.utils.ConsoleUtils;
import framework.utils.ValidationUtils;
import framework.variable.entity.MatrixVariable;
import framework.variable.entity.VectorVariable;
import framework.variable.holder.VariableHolder;
import framework.variable.entity.Variable;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Class is created to build and print greeting and manual
 */
public class ApplicationInfoPrinter {

    private final String greeting;

    private final CommandHolder commandHolder;

    private final VariableHolder variableHolder;

    public ApplicationInfoPrinter(Properties applicationProperties, CommandHolder commandHolder, VariableHolder variableHolder) {
        this.greeting = buildGreeting(applicationProperties);
        this.commandHolder = commandHolder;
        this.variableHolder = variableHolder;
    }

    public void printManual() {
        ConsoleUtils.println(buildManual());
    }

    public void printGreeting() {
        ConsoleUtils.println(this.greeting);
    }

    private String buildGreeting(Properties applicationProperties) {
        StringBuilder stringBuilder = new StringBuilder();
        appendApplicationPart(stringBuilder, applicationProperties);
        return stringBuilder.toString();
    }


    private String buildManual() {
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
        Map<String, Variable> variables = variableHolder.getVariables();
        destination.append(String.format("Variables:%n"));
        variables.values().stream()
                .sorted(Comparator.comparing(Variable::getName))
                .forEach((e) -> {
                    destination.append(String.format("* %s:%n", e.getName()));
                    destination.append(String.format("\tDescription: %s%n", e.getDescription()));
                    destination.append(String.format("\tType: %s%n", e.getType()));
                    if (e.getType() == VariableType.VECTOR && Objects.equals(e.getClass(), VectorVariable.class)) {
                        int length = ((VectorVariable) e).getLength();
                        if (length > 0) {
                            destination.append(String.format("\tLength: %d%n", length));
                        }
                    } else if (e.getType() == VariableType.MATRIX && Objects.equals(e.getClass(), MatrixVariable.class)) {
                        int rowCount = ((MatrixVariable) e).getRowCount();
                        if (rowCount > 0) {
                            destination.append(String.format("\tRow count: %d%n", rowCount));
                        }
                        int columnCount = ((MatrixVariable) e).getColumnCount();
                        if (columnCount > 0) {
                            destination.append(String.format("\tColumn count: %d%n", columnCount));
                        }
                    }
                });
        destination.append(System.lineSeparator());
    }

    /**
     * Appends 'commands' part and adds line separator character to the end
     */
    private void appendCommandsPart(StringBuilder destination, CommandHolder commandHolder) {
        Map<String, Command> commands = commandHolder.getCommands();
        destination.append(String.format("Commands:%n"));
        commands.values().stream()
                .sorted(Comparator.comparing(Command::getName))
                .forEach(e -> {
                    destination.append(String.format("* %s:%n", e.getName()));
                    destination.append(String.format("\tDescription: %s%n", e.getDescription()));
                    destination.append(String.format("\tArity: %s%n", e.getArity()));
                });
        destination.append(System.lineSeparator());
    }
}
