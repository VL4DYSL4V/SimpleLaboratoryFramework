package framework.application;

import framework.application.info.ApplicationInfoPrinter;
import framework.command.*;
import framework.enums.PropertyName;
import framework.exception.LaboratoryFrameworkException;
import framework.utils.ConsoleUtils;
import framework.utils.PropertyUtils;
import framework.variable.VariableDtoHolder;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class Application {

    private final Map<String, Command> commands;

    private final Properties applicationProperties;

    private final VariableDtoHolder variableDtoHolder;

    private final CommandDtoHolder commandDtoHolder;

    private Application(Map<String, Command> commands, Properties applicationProperties,
                        VariableDtoHolder variableDtoHolder, CommandDtoHolder commandDtoHolder) {
        this.commands = commands;
        this.applicationProperties = applicationProperties;
        this.variableDtoHolder = variableDtoHolder;
        this.commandDtoHolder = commandDtoHolder;
    }

    public void start() {
        String applicationName = applicationProperties.getProperty(PropertyName.APPLICATION_NAME.getName());
        String leftSideOfCommandLine = String.format("%s ->", applicationName);
        while (true) {
            ConsoleUtils.print(leftSideOfCommandLine);
            String input = ConsoleUtils.readLine();
            executeCommand(input, new String[]{});
        }
    }

    public void executeCommand(String commandName, String[] args) {
        final Command command = commands.get(commandName);
        if (command == null) {
            ConsoleUtils.print(String.format("Unknown command: %s%n", commandName));
            return;
        }
        command.execute();
    }

    public static final class ApplicationBuilder {

        private final Properties applicationProperties;

        private final VariableDtoHolder variableDtoHolder;

        private final CommandDtoHolder commandDtoHolder;

        private final ApplicationInfoPrinter infoPrinter;

        private final Map<String, Command> commands = new ConcurrentHashMap<>();

        /**
         * @param propertiesPath - Path in classpath resources to .properties configuration file
         * @throws LaboratoryFrameworkException if loading properties has failed
         */
        public ApplicationBuilder(String propertiesPath) throws LaboratoryFrameworkException {
            this.applicationProperties = PropertyUtils.readFromFile(propertiesPath);
            this.variableDtoHolder = new VariableDtoHolder(applicationProperties);
            this.commandDtoHolder = new CommandDtoHolder(applicationProperties);
            this.infoPrinter = new ApplicationInfoPrinter(applicationProperties, commandDtoHolder, variableDtoHolder);
            addDefaultCommands();
        }

        public ApplicationBuilder addCommand(String commandName, Command command) {
            commands.put(commandName, command);
            return this;
        }

        public Application build() {
            return new Application(commands, applicationProperties, variableDtoHolder, commandDtoHolder);
        }

        private void addDefaultCommands() {
            commands.put("help", new HelpCommand(infoPrinter));
            commands.put("greet", new GreetingCommand(infoPrinter));
            commands.put("exit", new ExitCommand());
        }
    }

}
