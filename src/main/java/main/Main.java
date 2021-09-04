package main;

import framework.application.Application;

public class Main {

    private static final String PATH_TO_PROPERTIES = "/laboratory-framework.properties";

    public static void main(String[] args) {
        Application application = new Application(PATH_TO_PROPERTIES);
        application.executeCommand("help");
    }

}
