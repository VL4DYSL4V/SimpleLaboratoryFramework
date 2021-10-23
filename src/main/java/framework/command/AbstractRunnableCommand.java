package framework.command;

import framework.command.entity.Command;
import framework.command.holder.CommandHolder;
import framework.command.holder.CommandHolderAware;
import framework.command.parser.ArgsParser;
import framework.state.ApplicationState;
import framework.state.ApplicationStateAware;
import framework.utils.ValidationUtils;
import lombok.Data;

import java.util.Map;

@Data
public abstract class AbstractRunnableCommand
        implements RunnableCommand, ApplicationStateAware, CommandHolderAware {

    protected CommandHolder commandHolder;

    protected ApplicationState applicationState;

    private final String name;

    @Override
    public void setCommandHolder(CommandHolder holder) {
        ValidationUtils.requireNonNull(holder);
        this.commandHolder = holder;
    }

    @Override
    public void setApplicationState(ApplicationState state) {
        ValidationUtils.requireNonNull(state);
        this.applicationState = state;
    }

    protected final Map<String, String> parseArgs (String[] args) {
        ValidationUtils.requireNonNull(commandHolder, "Command holder is not injected");
        Command command = commandHolder.getCommand(name);
        ValidationUtils.requireNonNull(command, String.format("No such command: %s", name));
        return ArgsParser.parseArgs(args, command.getOptions());
    }
}
