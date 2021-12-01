package framework.command;

import framework.application.info.ApplicationInfoPrinter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class GreetingCommand implements RunnableCommand {

    private static final String NAME = "greet";

    private final ApplicationInfoPrinter infoPrinter;

    @Override
    public void execute(String[] args) {
        infoPrinter.printGreeting();
    }

    @Nonnull
    @Override
    public String getName() {
        return NAME;
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Prints greetings";
    }
}
