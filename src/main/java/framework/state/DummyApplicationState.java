package framework.state;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DummyApplicationState extends AbstractApplicationState {

    private BigDecimal n;

    public DummyApplicationState() {
        super();
    }

    protected void initVariableNameToSettersMap() {
        variableNameToSetter.put("n", (name, value) -> {
            StateHelper.defaultSet(name, "n", value, BigDecimal.class,
                    (rawValue) -> (BigDecimal) rawValue, this::setN);
        });
    }

    protected void initVariableNameToGettersMap() {
        variableNameToGetter.put("n", this::getN);
    }
}
