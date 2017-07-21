package expressivo;

import java.util.Map;

public class Multiply implements Expression{
    private final Expression left;
    private final Expression right;
    
    public Multiply(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        
    }
    
    @Override
    public String toString() {
        return "(" + this.left.toString() + " " +
                "*" + " " + this.right.toString() + ")";
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Multiply)) {
            return false;
        }
        Multiply thatMultiply = (Multiply) thatObject;
        return this.left.equals(thatMultiply.left) &&
               this.right.equals(thatMultiply.right);
    }
    
    @Override
    public int hashCode() {
       int n = 37;
       n = n * 37 + left.hashCode();
       n = n * 37 + right.hashCode();
       return n;
    }

    @Override // dx (u*v) = u * dv/dx + v * du/dx 
    public Expression differentiate(Variable variable) {
        return new Plus(new Multiply(left , right.differentiate(variable)),new Multiply(right , left.differentiate(variable)));
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        // TODO Auto-generated method stub
        Multiply product = new Multiply(left.simplify(environment), right.simplify(environment));
        if (product.isNumeric()) {
             return new Number( "" +(((Number) product.left).getVal() * ((Number) product.right).getVal()));
        }
        return (Expression)product;
    }

    @Override
    public boolean isNumeric() {
        return left.isNumeric() && right.isNumeric();
    }
}
