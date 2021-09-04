package framework.variable.dto;

import framework.enums.VariableType;
import lombok.Data;

@Data
public class VariableDto {

    private final String name;

    private final VariableType type;

    private final String description;

    private final String constraintViolationMessage;

}
