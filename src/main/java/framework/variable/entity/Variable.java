package framework.variable.entity;

import framework.enums.VariableType;
import framework.utils.ValidationUtils;
import lombok.Data;

@Data
public class Variable {

    private final String name;

    private final VariableType type;

    private final String description;

    private final boolean cannotBeSetFromInput;

    private final String constraintViolationMessage;

    public Variable(String name, VariableType type, String description, boolean cannotBeSetFromInput, String constraintViolationMessage) {
        ValidationUtils.requireNotEmpty(name);
        ValidationUtils.requireNonNull(type);
        this.name = name;
        this.type = type;
        this.description = description;
        this.cannotBeSetFromInput = cannotBeSetFromInput;
        this.constraintViolationMessage = constraintViolationMessage;
    }
}
