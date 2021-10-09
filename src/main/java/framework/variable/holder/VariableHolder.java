package framework.variable.holder;

import framework.variable.parser.VariablesParser;
import framework.variable.entity.Variable;
import lombok.Getter;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;
import java.util.Properties;

@Getter
@ThreadSafe
public class VariableHolder {

    private final Map<String, Variable> variables;

    private final Object lock = new Object();

    public VariableHolder(Properties applicationProperties) {
        this.variables = VariablesParser.getVariableNameToVariable(applicationProperties);
    }

    public Variable getVariable(String variableName) {
        synchronized (lock) {
            return variables.get(variableName);
        }
    }

}
