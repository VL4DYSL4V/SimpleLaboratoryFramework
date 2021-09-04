package framework.command;

import framework.enums.VariableType;
import framework.exception.LaboratoryFrameworkException;
import framework.state.ApplicationState;
import framework.utils.ConsoleUtils;
import framework.variable.VariableDtoHolder;
import framework.variable.dto.VariableDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SetVariableCommand implements Command {

    private final VariableDtoHolder variableDtoHolder;

    private final ApplicationState applicationState;

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            ConsoleUtils.println("This command requires 1 argument: the name of variable to be set");
            return;
        }
        String variableName = args[0];
        VariableDto variableDto = variableDtoHolder.getVariableDto(variableName);
        if (variableDto == null) {
            ConsoleUtils.println("Unknown variable");
            return;
        }
        Object result = getValueByType(variableDto.getType());
        applicationState.setVariable(variableName, result);
    }

    private Object getValueByType(VariableType type) {
        switch (type) {
            case NUMBER:
                return ConsoleUtils.askForBigDecimalRepeatedly();
            default:
                throw new LaboratoryFrameworkException(String.format("Unsupported type: %s", type));
        }
    }
}
