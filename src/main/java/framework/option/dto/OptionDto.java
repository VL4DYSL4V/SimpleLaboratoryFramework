package framework.option.dto;

import lombok.Data;

import javax.annotation.concurrent.Immutable;

@Immutable
@Data
public class OptionDto {

    private final String name;

    private final int arity;

    private final String description;

    private final String constraintViolationMessage;

}
