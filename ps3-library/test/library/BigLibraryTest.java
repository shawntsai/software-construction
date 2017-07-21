package library;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

/**
 * Test suite for BigLibrary's stronger specs.
 */
public class BigLibraryTest {
    
    /* 
     * NOTE: use this file only for tests of BigLibrary.find()'s stronger spec.
     * Tests of all other Library operations should be in LibraryTest.java 
     */

    /*
     * Testing strategy
     * ==================
     * 
     * TODO: your testing strategy for BigLibrary.find() should go here.
     * Make sure you have partitions.
     */
    
    // TODO: put JUnit @Test methods here that you developed from your testing strategy
    @Test
    public void testExampleTest() {
        // this is just an example test, you should delete it
        Library library = new BigLibrary();
        assertEquals(Collections.emptyList(), library.find("This Test Is Just An Example"));
    }
    private final Book book1 = new Book("Harry Porter", Arrays.asList("shawn tsai"), 2016);
    private final Book book2 = new Book("Norwegian Wood", Arrays.asList("Haruki Murakami"), 1987);
    private final Book book3 = new Book("Harry Porter", Arrays.asList("shawn tsai"), 2018);
    @Test
    public void testFindMatchTitleWords() {
        Library library = new BigLibrary();
        library.buy(book1);
        assertEquals(Arrays.asList(book1), library.find("Harry"));
        assertEquals(Arrays.asList(book1), library.find("Porter"));
    }
    
    @Test
    public void testFindMatchAuthorNames() {
        Library library = new BigLibrary();
        library.buy(book1);
        assertEquals(Arrays.asList(book1), library.find("shawn"));
        assertEquals(Arrays.asList(book1), library.find("tsai"));
    }
    
    @Test
    public void testFindRankingMoreKeyWords() {
        Library library = new BigLibrary();
        library.buy(book1);
        library.buy(book2);
        assertEquals(2, library.find("Norwegian Wood Harry").size());
        assertEquals(Arrays.asList(book2, book1), library.find("Norwegian Wood Porter"));
        assertEquals(Arrays.asList(book2, book1), library.find("Norwegian Wood Haruki Harry"));
    }
    
    @Test
    public void testFindRankingMultipleContiguousKeyWords() {
        Library library = new BigLibrary();
        library.buy(book1);
        library.buy(book2);
        assertEquals(Arrays.asList(book2, book1), library.find("Norwegian Wood Porter Harry"));
    }
    
    @Test
    public void testFindRankingOlder() {
        Library library = new BigLibrary();
        library.buy(book1);
        library.buy(book3);
        assertEquals(Arrays.asList(book3, book1), library.find("Harray Porter")) ;
    }
    
    @Test
    public void testFindDifferntBooksRankingCheckedOut() {
        Library library = new BigLibrary();
        BookCopy copy = library.buy(book1);
        library.checkout(copy);
        library.buy(book2);
        assertEquals(Arrays.asList(book2, book1), library.find("Harry Norwegian")) ;
    }
    
    private final Book book4 = new Book("Love in a Fallen City", Arrays.asList("Ei leen Chang"), 2018);
    @Test
    public void testFindQuotationMarksAsContiguousWords() {
        Library library = new BigLibrary();
        library.buy(book4);
        assertEquals(Arrays.asList(book4), library.find("\"Love in a Fallen\""));
        assertEquals(Arrays.asList(book4), library.find("\"Fallen City\""));
        assertEquals(Arrays.asList(book4), library.find("\"in a Fallen\""));
        assertEquals(Arrays.asList(book4), library.find("\"Ei leen\""));
        assertEquals(Arrays.asList(book4), library.find("\"leen Chang\""));
    }

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
