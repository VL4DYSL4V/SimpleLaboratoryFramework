package framework.variable.entity;

import framework.enums.VariableType;
import lombok.Data;

@Data
public class Variable {

    private final String name;

    private final VariableType type;

    private final String description;

    private final String constraintViolationMessage;

}
