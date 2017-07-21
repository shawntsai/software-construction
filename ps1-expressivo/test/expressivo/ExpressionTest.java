/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    // Testing strategy
    //   TODO
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /**
     * zero, bigger than zero 
     *  
     */
    final Double max = Double.MAX_VALUE;
    final private Number zero = new Number("0");
    final private Number zero_f = new Number("0.00");
    final private Number one = new Number("1.00");
    final private Number maxNumber = new Number(max.toString());
    
    @Test
    public void testNumberEquals() {
        assertTrue(new Number("0").equals(zero));
        assertTrue(new Number("0").equals(zero_f));
        assertFalse(new Number("0").equals(one));
        assertFalse(new Number("1").equals(zero));
    }
    @Test
    public void testNumberToString() {
        assertEquals(new Double(0).toString(), new Number("0").toString());
        assertEquals(new Double(1).toString(), new Number("1").toString());
        assertEquals(max.toString(), maxNumber.toString());
    }
    @Test
    public void testNumberHashCode() {
        assertEquals(zero.hashCode(), zero_f.hashCode());
        assertEquals(one.hashCode(), one.hashCode());
        assertNotEquals(zero.hashCode(), one.hashCode());
        assertNotEquals(zero.hashCode(), maxNumber.hashCode());
    }
    
    private final Number n = new Number("1.23");
    private final Number o = new Number("4.56");
    private final Variable Xyz = new Variable("Xyz");
    private final Variable Tuv = new Variable("Tuv");

    @Test
    public void testMultiplyEquals() {
        assertTrue(new Multiply(n, Xyz).equals(new Multiply(n, Xyz)));
        assertTrue(new Multiply(n, o).equals(new Multiply(n, o)));
        assertFalse(new Multiply(Xyz, n).equals(new Multiply(n, Xyz)));
        assertFalse(new Multiply(o, n).equals(new Multiply(n, o)));
        assertTrue(new Multiply(Xyz, new Multiply(Xyz, n)).equals(new Multiply(Xyz, new Multiply(Xyz, n))));
        assertTrue(new Multiply(new Multiply(Xyz, n), Xyz).equals(new Multiply(new Multiply(Xyz, n), Xyz)));
        assertTrue(new Multiply(Xyz, new Plus(Xyz, n)).equals(new Multiply(Xyz, new Plus(Xyz, n))));
        assertTrue(new Multiply(new Plus(Xyz, n), Xyz).equals(new Multiply(new Plus(Xyz, n), Xyz)));
        assertFalse(new Multiply(new Plus(Xyz, n), Xyz).equals(new Multiply(Xyz, new Plus(Xyz, n))));
    }
    @Test   
    public void testMultiplyToString() {
        assertEquals("(1.23 * Xyz)", new Multiply(n, Xyz).toString());
        assertEquals("(Xyz * 1.23)", new Multiply(Xyz, n).toString());
        assertEquals("(Xyz * Tuv)", new Multiply(Xyz, Tuv).toString());
        assertEquals("(Xyz * (Xyz * 1.23))", new Multiply(Xyz, new Multiply(Xyz, n)).toString());
        assertEquals("((Xyz * 1.23) * Xyz)", new Multiply(new Multiply(Xyz, n), Xyz).toString());
        assertEquals("(Xyz * (Xyz + 1.23))", new Multiply(Xyz, new Plus(Xyz, n)).toString());
        assertEquals("((Xyz + 1.23) * Xyz)", new Multiply(new Plus(Xyz, n), Xyz).toString());
    }
    
    @Test   
    public void testMultiplyHashCode() {
       assertFalse(new Multiply(n, Xyz).hashCode() == new Multiply(Xyz, n).hashCode());
       assertTrue(new Multiply(n, Xyz).hashCode() == new Multiply(n, Xyz).hashCode());
       assertTrue(new Multiply(Xyz, new Multiply(Xyz, n)).hashCode() == new Multiply(Xyz, new Multiply(Xyz, n)).hashCode());
       assertTrue(new Multiply(new Multiply(Xyz, n), Xyz).hashCode() == new Multiply(new Multiply(Xyz, n), Xyz).hashCode());
       assertTrue(new Multiply(Xyz, new Plus(Xyz, n)).hashCode() == new Multiply(Xyz, new Plus(Xyz, n)).hashCode());
       assertTrue(new Multiply(new Plus(Xyz, n), Xyz).hashCode() == new Multiply(new Plus(Xyz, n), Xyz).hashCode());
    }
     
    @Test   
    public void testPlusEquals() {
        assertTrue(new Plus(n, Xyz).equals(new Plus(n, Xyz)));
        assertTrue(new Plus(n, o).equals(new Plus(n, o)));
        assertFalse(new Plus(Xyz, n).equals(new Plus(n, Xyz)));
        assertFalse(new Plus(o, n).equals(new Plus(n, o)));
        assertTrue(new Plus(Xyz, new Plus(Xyz, n)).equals(new Plus(Xyz, new Plus(Xyz, n))));
        assertTrue(new Plus(new Plus(Xyz, n), Xyz).equals(new Plus(new Plus(Xyz, n), Xyz)));
        assertTrue(new Plus(Xyz, new Multiply(Xyz, n)).equals(new Plus(Xyz, new Multiply(Xyz, n))));
        assertTrue(new Plus(new Multiply(Xyz, n), Xyz).equals(new Plus(new Multiply(Xyz, n), Xyz)));
        assertFalse(new Plus(new Multiply(Xyz, n), Xyz).equals(new Plus(Xyz, new Multiply(Xyz, n))));
    }
    
    @Test   
    public void testPlusToString() {
        assertEquals("(1.23 + Xyz)", new Plus(n, Xyz).toString());
        assertEquals("(Xyz + 1.23)", new Plus(Xyz, n).toString());
        assertEquals("(Xyz + Tuv)", new Plus(Xyz, Tuv).toString());
        assertEquals("(Xyz + (Xyz + 1.23))", new Plus(Xyz, new Plus(Xyz, n)).toString());
        assertEquals("((Xyz + 1.23) + Xyz)", new Plus(new Plus(Xyz, n), Xyz).toString());
        assertEquals("(Xyz + (Xyz * 1.23))", new Plus(Xyz, new Multiply(Xyz, n)).toString());
        assertEquals("((Xyz * 1.23) + Xyz)", new Plus(new Multiply(Xyz, n), Xyz).toString());
    }
    
    @Test   
    public void testPlusHashCode() {
       assertFalse(new Plus(n, Xyz).hashCode() == new Plus(Xyz, n).hashCode());
       assertTrue(new Plus(n, Xyz).hashCode() == new Plus(n, Xyz).hashCode());
       assertTrue(new Plus(Xyz, new Plus(Xyz, n)).hashCode() == new Plus(Xyz, new Plus(Xyz, n)).hashCode());
       assertTrue(new Plus(new Plus(Xyz, n), Xyz).hashCode() == new Plus(new Plus(Xyz, n), Xyz).hashCode());
       assertTrue(new Plus(Xyz, new Multiply(Xyz, n)).hashCode() == new Plus(Xyz, new Multiply(Xyz, n)).hashCode());
       assertTrue(new Plus(new Multiply(Xyz, n), Xyz).hashCode() == new Plus(new Multiply(Xyz, n), Xyz).hashCode());
    }
    
    @Test
    public void testParseNumber() throws Exception{
        Expression ex1 = Expression.parse("1");
        assertEquals(new Number("1"), ex1);
        assertEquals(new Number("1.0"), Expression.parse("1.0"));
        assertEquals(new Number("1"), Expression.parse("1.0"));
        assertEquals(new Number("1.0"), Expression.parse("1"));
    }
    
    @Test
    public void testParseVar() throws Exception {
        assertEquals(new Variable("x"), Expression.parse("x"));
        assertNotEquals(new Variable("X"), Expression.parse("x"));
    }
    
    @Test
    public void testParseSum() throws Exception {
        Expression exp1 = Expression.parse("1");
        Expression exp2 = Expression.parse("5");
        Expression exp3 = Expression.parse("x");
        Expression exp4 = Expression.parse("y");
        assertEquals(new Plus(exp1, exp2), Expression.parse("1 + 5"));
        assertEquals(new Plus(exp1, exp2), Expression.parse("1+5"));
        assertEquals(new Plus(exp1, exp2), Expression.parse("1      + 5"));
        assertEquals(new Plus(exp1, exp2), Expression.parse("(1) + 5"));
        assertEquals(new Plus(exp1, exp2), Expression.parse("1 + (5)"));
        assertEquals(new Plus(exp1, exp2), Expression.parse("(1 + 5)"));
        assertEquals(new Plus(exp1, exp2), Expression.parse("1.0 + 5"));
        assertEquals(new Plus(exp1, exp2), Expression.parse("((1) + 5)"));
        assertNotEquals(new Plus(exp1, exp2), Expression.parse("5 + 1"));
        assertEquals(new Plus(exp3, exp4), Expression.parse("x + y"));
        assertNotEquals(new Plus(exp3, exp4), Expression.parse("y + x"));
        assertEquals(new Plus(exp1, exp3), Expression.parse("(1) + x"));
        
    }
    @Test
    public void testParseMultiply() throws Exception {
        Expression exp1 = Expression.parse("1");
        Expression exp2 = Expression.parse("5");
        Expression exp3 = Expression.parse("x");
        Expression exp4 = Expression.parse("y");
        assertEquals(new Multiply(exp1, exp2), Expression.parse("1 * 5"));
        assertEquals(new Multiply(exp1, exp2), Expression.parse("1*5"));
        assertEquals(new Multiply(exp1, exp2), Expression.parse("1 *      5"));
        assertEquals(new Multiply(exp1, exp2), Expression.parse("(1) * 5"));
        assertEquals(new Multiply(exp1, exp2), Expression.parse("1 * (5)"));
        assertEquals(new Multiply(exp1, exp2), Expression.parse("(1 * 5)"));
        assertEquals(new Multiply(exp1, exp2), Expression.parse("1.0 * 5"));
        assertEquals(new Multiply(exp1, exp2), Expression.parse("((1) * 5)"));
        assertNotEquals(new Multiply(exp1, exp2), Expression.parse("5 * 1"));
        assertEquals(new Multiply(exp3, exp4), Expression.parse("x * y"));
        assertNotEquals(new Multiply(exp3, exp4), Expression.parse("y * x"));
        assertEquals(new Multiply(exp1, exp3), Expression.parse("(1) * x"));
    }
    
    @Test
    public void testParseMutlipleOperands() throws Exception {
         Expression exp = Expression.parse("1 + 2 + 3");
         Expression exp2 = Expression.parse("1 * 2 + 3");
         Expression exp3 = Expression.parse("1 + 2 * 3");
         Expression exp4 = Expression.parse("1 + (2 * 3)");
         Expression exp5 = Expression.parse("(1 + 2) * 3");
         assertEquals(new Plus(new Plus(new Number("1"), new Number("2")), new Number("3")), exp);
         assertEquals(new Plus(new Multiply(new Number("1"), new Number("2")), new Number("3")), exp2);
         assertEquals(new Plus(new Number("1"), new Multiply(new Number("2"), new Number("3"))), exp3);
         assertEquals(new Plus(new Number("1"), new Multiply(new Number("2"), new Number("3"))), exp4);
         assertEquals(new Multiply(new Plus(new Number("1"), new Number("2")), new Number("3")), exp5);
    }
}
