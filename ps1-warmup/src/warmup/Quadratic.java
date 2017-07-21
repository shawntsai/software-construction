package warmup;

import java.util.HashSet;
import java.util.Set;

public class Quadratic {

    /**
     * Find the integer roots of a quadratic equation, ax^2 + bx + c = 0.
     * @param a coefficient of x^2
     * @param b coefficient of x
     * @param c constant term.  Requires that a, b, and c are not ALL zero.
     * @return all integers x such that ax^2 + bx + c = 0.
     */
    public static Set<Integer> roots(int a, int b, int c) {
        // when c is close to 2^ 31 -1
        Set<Integer> r = new HashSet<>();
        Long temp = b *b - (long)4 * a * c;
        if (temp < 0) return r; 
            
        double result1 = -1.0 * b + Math.sqrt(temp);
        double result2 = -1.0 * b - Math.sqrt(temp);
        
        if (a == 0) {
            double degenerate = -1. *c / b;
            if (degenerate == (int) degenerate)
                r.add((int)degenerate);
            return r;
        }
        result1 /= (2.*a);
        result2 /= (2.*a);
        
        if (temp != 0) {
            if (result1 == (int) result1)
                r.add((int)result1);
            if (result2 == (int) result2)
                r.add((int)result2);
        }
        else {
            if (result1 == (int) result1)
                r.add((int)result1);
        }
        return r;
        
//        throw new RuntimeException("not implemented yet;"); // TODO: delete this line when you implement it
    }

    
    /**
     * Main function of program.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("For the equation x^2 - 4x + 3 = 0, the possible solutions are:");
        Set<Integer> result = roots(1, -4, 3);
        System.out.println(result);
    }

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
