package framework.application;

import framework.application.manual.ApplicationManualPrinter;
import framework.exception.LaboratoryFrameworkException;
import framework.command.CommandDtoHolder;
import framework.utils.PropertyUtils;

import java.util.Objects;
import java.util.Properties;

public class Application {

    private final Properties applicationProperties;
    private final CommandDtoHolder commandDtoHolder;
    private final ApplicationManualPrinter manualPrinter;

    /**
     * @param propertiesPath - Path in classpath resources to .properties configuration file
     * @throws LaboratoryFrameworkException if loading properties has failed
     */
    public Application(String propertiesPath) throws LaboratoryFrameworkException {
        this.applicationProperties = PropertyUtils.readFromFile(propertiesPath);
        this.commandDtoHolder = new CommandDtoHolder(applicationProperties);
        this.manualPrinter = new ApplicationManualPrinter(applicationProperties, commandDtoHolder);
    }

    public void executeCommand(String option) {
        if (Objects.equals(option, "help")){
            manualPrinter.printManual();
        }
    }

}
