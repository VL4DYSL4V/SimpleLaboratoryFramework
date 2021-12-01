package framework.command;

import javax.annotation.Nonnull;

public class ExitCommand implements RunnableCommand {

    private static final String NAME = "exit";

    @Override
    public void execute(String[] args) {
        System.exit(0);
    }

    @Nonnull
    @Override
    public String getName() {
        return NAME;
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Interrupt all work and exit without saving results";
    }
}
