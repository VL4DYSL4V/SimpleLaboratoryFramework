package framework.application;

import java.util.Properties;

public class ApplicationManualPrinter {

    private final String manual;

    public ApplicationManualPrinter(Properties applicationProperties) {
        this.manual = buildManual(applicationProperties);
    }

    private String buildManual(Properties applicationProperties) {
        StringBuilder stringBuilder = new StringBuilder();

        return stringBuilder.toString();
    }

}
