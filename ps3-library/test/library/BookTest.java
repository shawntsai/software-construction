package library;

import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for Book ADT.
 */
public class BookTest {

    /*
     * Testing strategy
     * ==================
     * 
     * one author, many authors
     *  
     * Make sure you have partitions.
     */
    
    // TODO: put JUnit @Test methods here that you developed from your testing strategy
    @Test
    public void testExampleTest() {
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        assertEquals("This Test Is Just An Example", book.getTitle());
    }
    
    @Test
    public void testOneAuthor() {
        Book book = new Book("Pride and Predudice", Arrays.asList("Jane"), 2003);
        assertEquals("Pride and Predudice", book.getTitle());
        assertEquals(2003, book.getYear());
        assertEquals(Arrays.asList("Jane"), book.getAuthors());
    }
    
    @Test
    public void testManyAuthorsAlphabetically() {
        Book book = new Book("*", Arrays.asList("b", "a", "c"), 4434);
        assertEquals("*", book.getTitle());
        assertEquals(Arrays.asList("a", "b", "c"), book.getAuthors());
    }


    @Test
    public void testManyAuthorsMixCase() {
        Book book = new Book("?", Arrays.asList("A", "c", "B"), 223);
        assertEquals(Arrays.asList("A", "B", "c"), book.getAuthors());
    }
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testBookString() {
        Book book = new Book("Pride and Predudice", Arrays.asList("Jane"), 2003);
        assertEquals("Pride and Predudice, Jane, 2003",book.toString());
    }

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
