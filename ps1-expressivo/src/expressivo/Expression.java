package expressivo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import lib6005.parser.GrammarCompiler;
import lib6005.parser.ParseTree;
import lib6005.parser.Parser;
import lib6005.parser.UnableToParseException;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS1 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    // Datatype definition
    // Expression <T> =  Number(n: double) + Plus(left: expression<T>, right: expression<T>) 
    // + Multiply(left: expression<T>, right: expression<T>)
    
    enum ExpressionGrammer {ROOT, SUM, PRIMITIVE, NUMBER, WHITESPACE, MUL, FACTOR, VAR};
    
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS1 handout.
     * @return expression AST for the input
     * @throws IOException 
     * @throws UnableToParseException 
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) {
//        throw new RuntimeException("unimplemented");
        try {
            Parser<ExpressionGrammer> parser = GrammarCompiler.compile(new File("src/expressivo/Expression.g"), Expression.ExpressionGrammer.ROOT);
            ParseTree<ExpressionGrammer> tree = parser.parse(input);
            return Expression.buildAST(tree);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
    
    public static Expression buildAST(ParseTree<ExpressionGrammer>tree) {
        switch(tree.getName()) {
        
        case ROOT:
            return buildAST(tree.childrenByName(ExpressionGrammer.SUM).get(0));
            
        case SUM:
            boolean first = true;
            Expression result = null;
            for(ParseTree<ExpressionGrammer> child : tree.childrenByName(ExpressionGrammer.PRIMITIVE)){                
                if(first){
                    result = buildAST(child);
                    first = false;
                }else{
                    result = new Plus(result, buildAST(child));
                }
            }
            if(first){ throw new RuntimeException("sum must have a non whitespace child:" + tree); }
            return result;

        case NUMBER:
            return new Number(tree.getContents());
            
        case MUL:
            /*
             * A sum will have one or more children that need to be summed together.
             * Note that we only care about the children that are primitive. There may also be 
             * some whitespace children which we want to ignore.
             */
            boolean first1 = true;
            Expression result1 = null;
            for(ParseTree<ExpressionGrammer> child : tree.childrenByName(ExpressionGrammer.FACTOR)){                
                if(first1){
                    result1 = buildAST(child);
                    first1 = false;
                }else{
                    result1 = new Multiply(result1, buildAST(child));
                }
            }
            if(first1){ throw new RuntimeException("sum must have a non whitespace child:" + tree); }
            return result1;
            
        case PRIMITIVE:
        case FACTOR:
            if (!tree.childrenByName(ExpressionGrammer.NUMBER).isEmpty()) {
                return buildAST(tree.childrenByName(ExpressionGrammer.NUMBER).get(0));
            }
            else if (!tree.childrenByName(ExpressionGrammer.VAR).isEmpty()) {
                return buildAST(tree.childrenByName(ExpressionGrammer.VAR).get(0));
            }
            else if (!tree.childrenByName(ExpressionGrammer.MUL).isEmpty()) {
                return buildAST(tree.childrenByName(ExpressionGrammer.MUL).get(0));
            }
            else {
                return buildAST(tree.childrenByName(ExpressionGrammer.SUM).get(0));
            }
            
        case VAR:
            return new Variable(tree.getContents());
           
        case WHITESPACE:
            throw new RuntimeException("You should never reach here:" + tree);
        }
        throw new RuntimeException("You should never reach here:" + tree);
    }
    public Expression differentiate(Variable variable);
    public Expression simplify(Map<String, Double> environment);
    boolean isNumeric();
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS1 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    // TODO more instance methods
    
    /* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires permission of course staff.
     */
}
