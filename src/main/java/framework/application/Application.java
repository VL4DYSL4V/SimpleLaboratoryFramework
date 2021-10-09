package framework.application;

import framework.application.info.ApplicationInfoPrinter;
import framework.command.*;
import framework.command.holder.CommandHolder;
import framework.command.holder.CommandHolderAware;
import framework.enums.PropertyName;
import framework.exception.LaboratoryFrameworkException;
import framework.state.ApplicationState;
import framework.state.ApplicationStateAware;
import framework.utils.ConsoleUtils;
import framework.utils.PropertyUtils;
import framework.variable.holder.VariableHolder;
import framework.variable.holder.VariableHolderAware;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class Application {

    private final Map<String, RunnableCommand> commands;

    private final Properties applicationProperties;

    private Application(Map<String, RunnableCommand> commands, Properties applicationProperties) {
        this.commands = commands;
        this.applicationProperties = applicationProperties;
    }

    public void start() {
        String applicationName = applicationProperties.getProperty(PropertyName.APPLICATION_NAME.getName());
        String leftSideOfCommandLine = String.format("%s ->", applicationName);
        while (true) {
            ConsoleUtils.print(leftSideOfCommandLine);
            listenForTheInput();
        }
    }

    private void listenForTheInput() {
        String input = ConsoleUtils.readLine().trim();
        if (!input.isEmpty()) {
            String[] parts = input.split(" ");
            String[] args = new String[parts.length - 1];
            System.arraycopy(parts, 1, args, 0, args.length);
            executeCommand(parts[0], args);
        }
    }

    public void executeCommand(String commandName, String[] args) {
        final RunnableCommand runnableCommand = commands.get(commandName);
        if (runnableCommand == null) {
            ConsoleUtils.print(String.format("Unknown command: %s%n", commandName));
            return;
        }
        runnableCommand.execute(args);
    }

    public static final class ApplicationBuilder {

        private final Properties applicationProperties;

        private final ApplicationState state;

        private final VariableHolder variableHolder;

        private final CommandHolder commandHolder;

        private final ApplicationInfoPrinter infoPrinter;

        private final Map<String, RunnableCommand> commands = new ConcurrentHashMap<>();

        /**
         * @param propertiesPath - Path in classpath resources to .properties configuration file
         * @throws LaboratoryFrameworkException if loading properties has failed
         */
        public ApplicationBuilder(String propertiesPath, ApplicationState state) throws LaboratoryFrameworkException {
            this.state = state;
            this.applicationProperties = getProperties(propertiesPath);
            this.variableHolder = new VariableHolder(applicationProperties);
            this.commandHolder = new CommandHolder(applicationProperties);
            this.infoPrinter = new ApplicationInfoPrinter(applicationProperties, commandHolder, variableHolder);
            injectHolders(state);
        }

        public ApplicationBuilder addCommand(String commandName, RunnableCommand runnableCommand) {
            commands.put(commandName, runnableCommand);
            injectHolders(runnableCommand);
            if (runnableCommand instanceof ApplicationStateAware) {
                ((ApplicationStateAware) runnableCommand).setApplicationState(state);
            }
            return this;
        }

        private void injectHolders(Object target) {
            if (target instanceof VariableHolderAware) {
                ((VariableHolderAware) target).setVariableHolder(variableHolder);
            }
            if (target instanceof CommandHolderAware) {
                ((CommandHolderAware) target).setCommandHolder(commandHolder);
            }
        }

        public Application build() {
            addDefaultCommands();
            return new Application(commands, applicationProperties);
        }

        private void addDefaultCommands() {
            addCommand("help", new HelpCommand(infoPrinter));
            addCommand("greet", new GreetingCommand(infoPrinter));
            addCommand("exit", new ExitCommand());
            addCommand("set", new SetVariableCommand());
            addCommand("get", new GetVariableCommand());
        }

        private Properties getProperties(String propertiesPath) {
            Properties properties = new Properties();

            properties.setProperty("command.help.name", "help");
            properties.setProperty("command.help.description", "Prints all commands with arity and description");

            properties.setProperty("command.greet.name", "greet");
            properties.setProperty("command.greet.description", "Prints greetings");

            properties.setProperty("command.get.name", "get");
            properties.setProperty("command.get.arity", "1");
            properties.setProperty("command.get.description", "Returns value of variable with supplied name. Example: get variable-name");
            properties.setProperty("command.get.constraint-violation-message", "Command requires 1 argument: the name of variable to get");

            properties.setProperty("command.set.name", "set");
            properties.setProperty("command.set.arity", "1");
            properties.setProperty("command.set.description", "Invokes setting variable mechanism. Example: set variable-name");
            properties.setProperty("command.set.constraint-violation-message", "Command requires 1 argument: the name of variable to be set");

            properties.setProperty("command.exit.name", "exit");
            properties.setProperty("command.exit.description", "Interrupt all work and exit without saving results");

            PropertyUtils.readFromFile(propertiesPath, properties);
            return properties;
        }
    }

}
