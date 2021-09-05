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
            this.applicationProperties = PropertyUtils.readFromFile(propertiesPath);
            this.variableHolder = new VariableHolder(applicationProperties);
            this.commandHolder = new CommandHolder(applicationProperties);
            this.infoPrinter = new ApplicationInfoPrinter(applicationProperties, commandHolder, variableHolder);
        }

        public ApplicationBuilder addCommand(String commandName, RunnableCommand runnableCommand) {
            commands.put(commandName, runnableCommand);
            if (runnableCommand instanceof VariableHolderAware) {
                ((VariableHolderAware) runnableCommand).setVariableHolder(variableHolder);
            }
            if (runnableCommand instanceof CommandHolderAware) {
                ((CommandHolderAware) runnableCommand).setCommandHolder(commandHolder);
            }
            if (runnableCommand instanceof ApplicationStateAware) {
                ((ApplicationStateAware) runnableCommand).setApplicationState(state);
            }
            return this;
        }

        public Application build() {
            addDefaultCommands();
            return new Application(commands, applicationProperties);
        }

        private void addDefaultCommands() {
            addCommand("help", new HelpRunnableCommand(infoPrinter));
            addCommand("greet", new GreetingRunnableCommand(infoPrinter));
            addCommand("exit", new ExitRunnableCommand());
            addCommand("set", new SetVariableRunnableCommand());
        }
    }

}
