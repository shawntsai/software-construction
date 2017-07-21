/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Testing strategy
    //   TODO
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    // TODO tests for Commands.differentiate() and Commands.simplify()
    @Test
    public void testDifferentiateVar() throws Exception{
        assertEquals("1.0", Commands.differentiate("x", "x"));
        assertEquals("0.0", Commands.differentiate("x", "y"));
    }
    
    @Test
    public void testDifferentiateNumber() throws Exception {
        assertEquals("0.0", Commands.differentiate("3", "y"));
        assertEquals("0.0", Commands.differentiate("0.0", "y"));
    }
    
    @Test
    public void testDifferentiateSum() throws Exception {
        assertEquals("(1.0 + 0.0)", Commands.differentiate("x + 1", "x"));
        assertEquals("(0.0 + 0.0)", Commands.differentiate("1 + 1", "x"));
        assertEquals("(0.0 + 1.0)", Commands.differentiate("1 + x", "x"));
    }
    
    @Test // d/dx(u*v) = u * dv/dx + v * du/dx
    public void testDifferentitateMultiply() throws Exception {
        assertEquals("((x * 1.0) + (x * 1.0))", Commands.differentiate("x*x", "x"));
        assertEquals("((2.0 * 1.0) + (x * 0.0))", Commands.differentiate("2*x", "x"));
//        assertEquals(""+Float.MAX_VALUE, Commands.differentiate("" +Float.MAX_VALUE+"*x", "x"));
    }
    
    @Test
    public void testSimplifyWithoutMap() throws Exception {
        assertEquals("4.0", Commands.simplify("1+3", new HashMap<String, Double>()));
        assertEquals("4.0", Commands.simplify("4", new HashMap<String, Double>()));
        assertEquals("4.0", Commands.simplify("1+1+1+1", new HashMap<String, Double>()));
        assertEquals("4.0", Commands.simplify("2*2", new HashMap<String, Double>()));
        assertEquals("4.0", Commands.simplify("4*1.0", new HashMap<String, Double>()));
    }
    
    @Test
    public void testSimplifyPlus() throws Exception {
        assertEquals("3.0", Commands.simplify("x+2", new HashMap<String, Double>(){{put("x", 1.0);}}));
        assertEquals("2.0", Commands.simplify("x+x", new HashMap<String, Double>(){{put("x", 1.0);}}));
        assertEquals("(1.0 + y)", Commands.simplify("x+y", new HashMap<String, Double>(){{put("x", 1.0);}}));
    }
    
    @Test
    public void testSimplifyMult() throws Exception {
        assertEquals("10.0", Commands.simplify("x*5", new HashMap<String, Double>(){{put("x", 2.0); }}));
        assertEquals("9.0", Commands.simplify("x*x", new HashMap<String, Double>(){{put("x", 3.0);}}));
        assertEquals("(3.0 * y)", Commands.simplify("x*y", new HashMap<String, Double>(){{put("x", 3.0);}}));
    }
    
}
