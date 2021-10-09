package framework.variable.entity;

import framework.enums.VariableType;
import framework.utils.ValidationUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class VectorVariable extends Variable {

    private int length;

    public VectorVariable(String name, VariableType type, String description, boolean cannotBeSetFromInput,
                          String constraintViolationMessage, int length) {
        super(name, type, description, cannotBeSetFromInput, constraintViolationMessage);
        ValidationUtils.requireGreaterOrEqualThan(length, 1,
                "Vector length must be >= 1");
        this.length = length;
    }

}
