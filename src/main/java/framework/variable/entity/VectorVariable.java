package framework.variable.entity;

import framework.enums.VariableType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class VectorVariable extends Variable {

    private final int length;

    public VectorVariable(String name, VariableType type, String description, String constraintViolationMessage, int length) {
        super(name, type, description, constraintViolationMessage);
        this.length = length;
    }

}
