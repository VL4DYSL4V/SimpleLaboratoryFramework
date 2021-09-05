package framework.command;

import framework.enums.VariableType;
import framework.exception.LaboratoryFrameworkException;
import framework.state.ApplicationState;
import framework.state.ApplicationStateAware;
import framework.utils.ConsoleUtils;
import framework.variable.holder.VariableHolder;
import framework.variable.entity.Variable;
import framework.variable.holder.VariableHolderAware;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@RequiredArgsConstructor
public class SetVariableRunnableCommand implements RunnableCommand, VariableHolderAware, ApplicationStateAware {

    private VariableHolder variableHolder;

    private ApplicationState applicationState;

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            ConsoleUtils.println("This command requires 1 argument: the name of variable to be set");
            return;
        }
        String variableName = args[0];
        Variable variable = variableHolder.getVariableDto(variableName);
        if (variable == null) {
            ConsoleUtils.println("Unknown variable");
            return;
        }
        Object result = getValueByType(variable.getType());
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
