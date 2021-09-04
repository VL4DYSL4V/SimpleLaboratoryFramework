package framework.application;

import framework.application.info.ApplicationInfoPrinter;
import framework.command.*;
import framework.command.holder.CommandDtoHolder;
import framework.command.holder.CommandHolderAware;
import framework.enums.PropertyName;
import framework.exception.LaboratoryFrameworkException;
import framework.state.ApplicationState;
import framework.state.ApplicationStateAware;
import framework.utils.ConsoleUtils;
import framework.utils.PropertyUtils;
import framework.variable.holder.VariableDtoHolder;
import framework.variable.holder.VariableHolderAware;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class Application {

    private final Map<String, Command> commands;

    private final Properties applicationProperties;

    private Application(Map<String, Command> commands, Properties applicationProperties) {
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
        String input = ConsoleUtils.readLine();
        if (!input.isEmpty()) {
            String[] parts = input.split(" ");
            String[] args = new String[parts.length - 1];
            System.arraycopy(parts, 1, args, 0, args.length);
            executeCommand(parts[0], args);
        }
    }

    public void executeCommand(String commandName, String[] args) {
        final Command command = commands.get(commandName);
        if (command == null) {
            ConsoleUtils.print(String.format("Unknown command: %s%n", commandName));
            return;
        }
        command.execute(args);
    }

    public static final class ApplicationBuilder {

        private final Properties applicationProperties;

        private final ApplicationState state;

        private final VariableDtoHolder variableDtoHolder;

        private final CommandDtoHolder commandDtoHolder;

        private final ApplicationInfoPrinter infoPrinter;

        private final Map<String, Command> commands = new ConcurrentHashMap<>();

        /**
         * @param propertiesPath - Path in classpath resources to .properties configuration file
         * @throws LaboratoryFrameworkException if loading properties has failed
         */
        public ApplicationBuilder(String propertiesPath, ApplicationState state) throws LaboratoryFrameworkException {
            this.state = state;
            this.applicationProperties = PropertyUtils.readFromFile(propertiesPath);
            this.variableDtoHolder = new VariableDtoHolder(applicationProperties);
            this.commandDtoHolder = new CommandDtoHolder(applicationProperties);
            this.infoPrinter = new ApplicationInfoPrinter(applicationProperties, commandDtoHolder, variableDtoHolder);
        }

        public ApplicationBuilder addCommand(String commandName, Command command) {
            commands.put(commandName, command);
            if (command instanceof VariableHolderAware) {
                ((VariableHolderAware) command).setVariableHolder(variableDtoHolder);
            }
            if (command instanceof CommandHolderAware) {
                ((CommandHolderAware) command).setCommandHolder(commandDtoHolder);
            }
            if (command instanceof ApplicationStateAware) {
                ((ApplicationStateAware) command).setApplicationState(state);
            }
            return this;
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
        }
    }

}
