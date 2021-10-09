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
public class PolynomialFunctionVariable extends Variable {

    private int maxDegree;

    public PolynomialFunctionVariable(String name, VariableType type, String description, boolean cannotBeSetFromInput,
                          String constraintViolationMessage, int maxDegree) {
        super(name, type, description, cannotBeSetFromInput, constraintViolationMessage);
        ValidationUtils.requireGreaterOrEqualThan(maxDegree, 0, "Max polynomial degree must be >= 0");
        this.maxDegree = maxDegree;
    }

}
