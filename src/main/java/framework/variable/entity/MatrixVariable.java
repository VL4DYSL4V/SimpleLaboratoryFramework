package framework.variable.entity;

import framework.enums.VariableType;
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

    public MatrixVariable(String name, VariableType type, String description, String constraintViolationMessage, int rowCount, int columnCount) {
        super(name, type, description, constraintViolationMessage);
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

}
