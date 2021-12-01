package framework.command;

import framework.application.info.ApplicationInfoPrinter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class HelpCommand implements RunnableCommand {

    private static final String NAME = "help";

    private final ApplicationInfoPrinter infoPrinter;

    public void execute(String[] args) {
        infoPrinter.printManual();
    }

    @Nonnull
    @Override
    public String getName() {
        return NAME;
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Prints all commands and variables with description";
    }
}
