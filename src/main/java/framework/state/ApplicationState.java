package framework.state;

public interface ApplicationState {

    void setVariable(String variableName, Object value);

    Object getVariable(String variableName);

}
