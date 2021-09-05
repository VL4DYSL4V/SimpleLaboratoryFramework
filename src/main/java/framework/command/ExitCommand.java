package framework.command;

public class ExitCommand implements RunnableCommand {

    @Override
    public void execute(String[] args) {
        System.exit(0);
    }

}
