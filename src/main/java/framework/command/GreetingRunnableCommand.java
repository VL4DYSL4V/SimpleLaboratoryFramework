package framework.command;

import framework.application.info.ApplicationInfoPrinter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GreetingRunnableCommand implements RunnableCommand {

    private final ApplicationInfoPrinter infoPrinter;

    @Override
    public void execute(String[] args) {
        infoPrinter.printGreeting();
    }

}
