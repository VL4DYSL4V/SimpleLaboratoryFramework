package framework.command;

public class ExitRunnableCommand implements RunnableCommand {

    @Override
    public void execute(String[] args) {
        System.exit(0);
    }

}
