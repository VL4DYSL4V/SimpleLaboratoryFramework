package framework.command;

import javax.annotation.Nonnull;

public abstract class DefaultRunCommand extends AbstractRunnableCommand {

    public DefaultRunCommand() {
        super("run");
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Run laboratory";
    }
}
