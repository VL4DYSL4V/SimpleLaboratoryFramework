package framework.command.holder;

import framework.command.entity.Command;
import framework.command.parser.CommandsParser;
import lombok.Getter;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;
import java.util.Properties;

/**
 * Class holds a map of command names to {@link Command}
 */
@ThreadSafe
@Getter
public final class CommandHolder {

    private final Map<String, Command> commands;

    private final Object lock = new Object();

    public CommandHolder(Properties applicationProperties) {
        this.commands = CommandsParser.getCommandNameToCommand(applicationProperties);
    }

    public Command getCommand(String commandName) {
        synchronized (lock) {
            return commands.get(commandName);
        }
    }

}
