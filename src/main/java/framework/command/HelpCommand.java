package framework.command;

import framework.application.info.ApplicationInfoPrinter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HelpCommand implements RunnableCommand {

    private final ApplicationInfoPrinter infoPrinter;

    public void execute(String[] args) {
        infoPrinter.printManual();
    }

}
