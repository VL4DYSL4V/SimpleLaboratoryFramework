package framework.command;

import framework.application.info.ApplicationInfoPrinter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HelpCommand implements Command {

    private final ApplicationInfoPrinter infoPrinter;

    public void execute() {
        infoPrinter.printManual();
    }

}
