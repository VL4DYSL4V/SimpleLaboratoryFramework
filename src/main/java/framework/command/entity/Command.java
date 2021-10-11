package framework.command.entity;

import framework.utils.ValidationUtils;
import lombok.Data;

import javax.annotation.concurrent.Immutable;
import java.util.Set;

@Immutable
@Data
public class Command {

    private final String name;

    private final Set<String> options;

    private final String description;

    private final String constraintViolationMessage;

    public Command(String name, Set<String> options, String description, String constraintViolationMessage) {
        ValidationUtils.requireNotEmpty(name);
        this.name = name;
        this.options = options;
        this.description = description;
        this.constraintViolationMessage = constraintViolationMessage;
    }
}
