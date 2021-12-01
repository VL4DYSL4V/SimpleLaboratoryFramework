package framework.command.holder;

import framework.command.NamedCommand;
import lombok.Getter;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class holds a map of command names to {@link NamedCommand}
 */
@ThreadSafe
@Getter
public final class CommandHolder {

    private final Map<String, ? extends NamedCommand> commands;

    public CommandHolder(Map<String, ? extends NamedCommand> commands) {
        this.commands = new ConcurrentHashMap<>(commands);
    }

    public NamedCommand getCommand(String commandName) {
        return commands.get(commandName);
    }

}
