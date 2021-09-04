package framework.command.dto;

import lombok.Data;

import javax.annotation.concurrent.Immutable;

@Immutable
@Data
public class CommandDto {

    private final String name;

    private final int arity;

    private final String description;

    private final String constraintViolationMessage;

}
