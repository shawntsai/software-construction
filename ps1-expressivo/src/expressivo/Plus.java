package expressivo;

import java.util.Map;

public class Plus implements Expression{
    private final Expression left;
    private final Expression right;
    
    public Plus(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public String toString() {
        return "(" + this.left.toString() +
                " + " + this.right.toString() +
                ")";
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Plus)) {
            return false;
        }
        Plus thatPlus = (Plus) thatObject;
        return this.left.equals(thatPlus.left) &&
               this.right.equals(thatPlus.right);
    }
    
    @Override
    public int hashCode() {
       int n = 37;
       n = n * 37 + left.hashCode();
       n = n * 37 + right.hashCode();
       return n;
    }

    @Override
    public Expression differentiate(Variable variable) {
        return new Plus(left.differentiate(variable), right.differentiate(variable));
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        Plus addition = new Plus(left.simplify(environment), right.simplify(environment));
        if (addition.isNumeric()) {
            return new Number("" + (((Number) addition.left).getVal() + ((Number) addition.right).getVal()));
        }
        return (Expression) addition;
    }

    @Override
    public boolean isNumeric() {
        return left.isNumeric() && right.isNumeric();
    }
}
