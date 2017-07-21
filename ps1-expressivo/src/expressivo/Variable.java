package expressivo;

import java.util.Map;
import java.util.Objects;

public class Variable implements Expression{
    final private String variable;
    
    public Variable(String variable) {
        this.variable = new String(variable);
        checkRep();
    }
    
    @Override public String toString() {
        return variable;
    }
    
    @Override public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Variable)) {
            return false;
        }
        Variable thatVariable = (Variable) thatObject;
        return variable.equals(thatVariable.variable);
    }
    
    @Override public int hashCode() {
        return Objects.hashCode(variable);
    }
    
    private void checkRep() {
        assert variable.matches("[a-zA-Z]+");
    }

    @Override
    public Expression differentiate(Variable variable) {
        if (this.variable.equals(variable.variable)) {
            return new Number("1");
        }
        else {
            return new Number("0");
        }
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        if (environment.containsKey(variable)) {
            return new Number("" + environment.get(variable));
        }
        else {
            return this;
        }
    }

    @Override
    public boolean isNumeric() {
        return false;
    }

}
