package framework.command;

public interface RunnableCommand extends NamedCommand {

    void execute(String[] args);

}
