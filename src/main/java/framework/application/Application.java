package framework.application;

import framework.application.manual.ApplicationInfoPrinter;
import framework.command.CommandDtoHolder;
import framework.exception.LaboratoryFrameworkException;
import framework.utils.PropertyUtils;
import framework.variable.VariableDtoHolder;

import java.util.Objects;
import java.util.Properties;

public class Application {

    private final Properties applicationProperties;
    private final VariableDtoHolder variableDtoHolder;
    private final CommandDtoHolder commandDtoHolder;
    private final ApplicationInfoPrinter infoPrinter;

    /**
     * @param propertiesPath - Path in classpath resources to .properties configuration file
     * @throws LaboratoryFrameworkException if loading properties has failed
     */
    public Application(String propertiesPath) throws LaboratoryFrameworkException {
        this.applicationProperties = PropertyUtils.readFromFile(propertiesPath);
        this.variableDtoHolder = new VariableDtoHolder(applicationProperties);
        this.commandDtoHolder = new CommandDtoHolder(applicationProperties);
        this.infoPrinter = new ApplicationInfoPrinter(applicationProperties, commandDtoHolder, variableDtoHolder);
    }

    public void executeCommand(String command) {
        if (Objects.equals(command, "help")) {
            infoPrinter.printManual();
        } else if (Objects.equals(command, "greeting")) {
            infoPrinter.printGreeting();
        }
    }

}
