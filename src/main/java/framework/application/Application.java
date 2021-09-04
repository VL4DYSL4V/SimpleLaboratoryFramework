package framework.application;

import framework.exception.LaboratoryFrameworkException;
import framework.utils.PropertyUtils;

import java.util.Properties;

public class Application {

    private final Properties applicationProperties;
    private final ApplicationManualPrinter manualPrinter;

    /**
     * @param propertiesPath - Path in classpath resources to .properties configuration file
     * @throws LaboratoryFrameworkException if loading properties has failed
     */
    public Application(String propertiesPath) throws LaboratoryFrameworkException {
        this.applicationProperties = PropertyUtils.readFromFile(propertiesPath);
        this.manualPrinter = new ApplicationManualPrinter(applicationProperties);
    }

}
