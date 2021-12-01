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
import java.util.NoSuchElementException;
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
            String[] parts = input.split("\\s+");
            if (parts.length > 0) {
                String[] args = new String[parts.length - 1];
                System.arraycopy(parts, 1, args, 0, args.length);
                executeCommand(parts[0], args);
            }
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

        private static final String DEFAULT_PROPERTY_PATH_STRING = "/laboratory.properties";

        private final String propertiesPath;

        private final ApplicationState state;

        private final Map<String, RunnableCommand> commands = new ConcurrentHashMap<>();

        public ApplicationBuilder(ApplicationState state) throws LaboratoryFrameworkException {
            this(DEFAULT_PROPERTY_PATH_STRING, state);
        }

        /**
         * @param propertiesPath - Path in classpath resources to .properties configuration file
         * @throws LaboratoryFrameworkException if loading properties has failed
         */
        public ApplicationBuilder(String propertiesPath, ApplicationState state) throws LaboratoryFrameworkException {
            this.propertiesPath = propertiesPath;
            this.state = state;
        }

        public ApplicationBuilder addCommand(RunnableCommand command) {
            commands.put(command.getName(), command);
            return this;
        }

        private void injectHolders(Object target, ApplicationState state, VariableHolder variableHolder,
                                   CommandHolder commandHolder) {
            if (target instanceof ApplicationStateAware) {
                ((ApplicationStateAware) target).setApplicationState(state);
            }
            if (target instanceof VariableHolderAware) {
                ((VariableHolderAware) target).setVariableHolder(variableHolder);
            }
            if (target instanceof CommandHolderAware) {
                ((CommandHolderAware) target).setCommandHolder(commandHolder);
            }
        }

        public Application build() {
            Properties applicationProperties = PropertyUtils.readFromFile(propertiesPath);
            final VariableHolder variableHolder = new VariableHolder(applicationProperties);
            ApplicationInfoPrinter infoPrinter = new ApplicationInfoPrinter(applicationProperties);

            addDefaultCommands(infoPrinter);
            final CommandHolder commandHolder = new CommandHolder(commands);

            injectHolders(state, state, variableHolder, commandHolder);
            injectHolders(infoPrinter, state, variableHolder, commandHolder);
            commands.values().forEach(e -> injectHolders(e, state, variableHolder, commandHolder));
            return new Application(commands, applicationProperties);
        }

        private void addDefaultCommands(ApplicationInfoPrinter infoPrinter) {
            addCommand(new HelpCommand(infoPrinter));
            addCommand(new GreetingCommand(infoPrinter));
            addCommand(new ExitCommand());
            addCommand(new SetVariableCommand());
            addCommand(new GetVariableCommand());
        }

    }

}
