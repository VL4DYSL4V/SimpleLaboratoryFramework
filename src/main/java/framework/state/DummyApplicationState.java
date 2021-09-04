package framework.state;

import framework.utils.ConsoleUtils;

import java.math.BigDecimal;
import java.util.Objects;

public class DummyApplicationState implements ApplicationState {

    private BigDecimal n;

    @Override
    public void setVariable(String variableName, Object value) {
        if (Objects.equals("n", variableName)) {
            if (Objects.equals(BigDecimal.class, value.getClass())) {
                this.n = (BigDecimal) value;
                ConsoleUtils.println("Success");
            } else {
                ConsoleUtils.println("Invalid type");
            }
        } else {
            ConsoleUtils.println("Wrong name");
        }
    }
}
