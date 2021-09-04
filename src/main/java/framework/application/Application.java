package framework.application;

import framework.application.manual.ApplicationManualPrinter;
import framework.exception.LaboratoryFrameworkException;
import framework.option.OptionHolder;
import framework.utils.PropertyUtils;

import java.util.Objects;
import java.util.Properties;

public class Application {

    private final Properties applicationProperties;
    private final OptionHolder optionHolder;
    private final ApplicationManualPrinter manualPrinter;

    /**
     * @param propertiesPath - Path in classpath resources to .properties configuration file
     * @throws LaboratoryFrameworkException if loading properties has failed
     */
    public Application(String propertiesPath) throws LaboratoryFrameworkException {
        this.applicationProperties = PropertyUtils.readFromFile(propertiesPath);
        this.optionHolder = new OptionHolder(applicationProperties);
        this.manualPrinter = new ApplicationManualPrinter(applicationProperties, optionHolder);
    }

    public void executeCommand(String option) {
        if (Objects.equals(option, "help")){
            manualPrinter.printManual();
        }
    }

}
