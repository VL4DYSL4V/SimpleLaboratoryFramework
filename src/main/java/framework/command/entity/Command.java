package framework.command.entity;

import framework.utils.ValidationUtils;
import lombok.Data;

import javax.annotation.concurrent.Immutable;

@Immutable
@Data
public class Command {

    private final String name;

    private final int arity;

    private final String description;

    private final String constraintViolationMessage;

    public Command(String name, int arity, String description, String constraintViolationMessage) {
        ValidationUtils.requireNotEmpty(name);
        this.name = name;
        this.arity = arity;
        this.description = description;
        this.constraintViolationMessage = constraintViolationMessage;
    }
}
