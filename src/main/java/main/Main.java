package main;

import framework.application.Application;
import framework.state.ApplicationState;
import framework.state.DummyApplicationState;

public class Main {

    private static final String PATH_TO_PROPERTIES = "/laboratory-framework.properties";

    public static void main(String[] args) {
        ApplicationState state = new DummyApplicationState();
        Application application = new Application.ApplicationBuilder(PATH_TO_PROPERTIES, state)
                .build();
        application.start();
    }

}
