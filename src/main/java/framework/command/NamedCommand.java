package framework.command;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public interface NamedCommand {

    @Nonnull
    String getName();

    @Nonnull
    default String getDescription() {
        return getName();
    }

    @Nonnull
    default Set<String> getOptions() {
        return new HashSet<>();
    }

    @Nonnull
    default String getConstraintViolationMessage() {
        return "Command can't be executed, check variables";
    }

}
