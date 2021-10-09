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
public class MatrixVariable extends Variable {

    private int rowCount;

    private int columnCount;

    public MatrixVariable(String name, VariableType type, String description, boolean cannotBeSetFromInput,
                          String constraintViolationMessage, int rowCount, int columnCount) {
        super(name, type, description, cannotBeSetFromInput, constraintViolationMessage);
        ValidationUtils.requireGreaterOrEqualThan(rowCount, 1, "Matrix row count must be >= 1");
        ValidationUtils.requireGreaterOrEqualThan(columnCount, 1, "Matrix column count must be >= 1");
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

}
