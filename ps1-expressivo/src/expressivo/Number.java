package expressivo;

import java.util.Map;

public class Number implements Expression{
    /* Rep invariant: n >= 0 and n <= Double.MAX_VALUE
     * Abstraction function: represents number in math expression 
     * 
     */
    private final double n;
    public Number(String number) {
        this.n = Double.parseDouble(number);
        checkRep();
    }
    public double getVal() {return n; } 
    private void checkRep() {
        assert n >= 0;
        assert n <= Double.MAX_VALUE;
    }
    
    @Override 
    public String toString() {
        return String.valueOf(n);
    }

    
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Number)) {
           return false;
        }
        Number thatNumber = (Number) thatObject;
        return n == thatNumber.n;
    }
    
    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(n);
        return (int) (bits ^ (bits >>> 32));
    }

    @Override
    public Expression differentiate(Variable variable) {
        return new Number("0");
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        return this;
    }

    @Override
    public boolean isNumeric() {
        return true;
    }
    
}
