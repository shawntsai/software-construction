package library;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test suite for Library ADT.
 */
@RunWith(Parameterized.class)
public class LibraryTest {

    /*
     * Note: all the tests you write here must be runnable against any
     * Library class that follows the spec.  JUnit will automatically
     * run these tests against both SmallLibrary and BigLibrary.
     */

    /**
     * Implementation classes for the Library ADT.
     * JUnit runs this test suite once for each class name in the returned array.
     * @return array of Java class names, including their full package prefix
     */
    @Parameters(name="{0}")
    public static Object[] allImplementationClassNames() {
        return new Object[] { 
            "library.SmallLibrary", 
            "library.BigLibrary"
        }; 
    }

    /**
     * Implementation class being tested on this run of the test suite.
     * JUnit sets this variable automatically as it iterates through the array returned
     * by allImplementationClassNames.
     */
    @Parameter
    public String implementationClassName;    

    /**
     * @return a fresh instance of a Library, constructed from the implementation class specified
     * by implementationClassName.
     */
    public Library makeLibrary() {
        try {
            Class<?> cls = Class.forName(implementationClassName);
            return (Library) cls.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /*
     * Testing strategy
     * ==================
     * 
     * TODO: your testing strategy for this ADT should go here.
     * Make sure you have partitions.
     */
    
    // TODO: put JUnit @Test methods here that you developed from your testing strategy
    @Test
    public void testExampleTest() {
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        assertEquals(Collections.emptySet(), library.availableCopies(book));
    }
    
     
    
    private final Book book1 = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
    @Test
    public void testBuy() {
        Library library = makeLibrary();
        BookCopy copy = library.buy(book1);
        HashSet<BookCopy> copies = new HashSet<>();
        copies.add(copy);
        assertEquals(copies, library.availableCopies(book1));
    }

    @Test
    public void testCeckout() {
        Library library = makeLibrary();
        BookCopy copy = library.buy(book1);
        library.checkout(copy);
        assertEquals(Collections.emptySet(), library.availableCopies(book1));
    }
    
    @Test
    public void testCheckin() {
        Library library = makeLibrary();
        BookCopy copy = library.buy(book1);
        library.checkout(copy);
        library.checkin(copy);
        assertTrue("check copy is in library", library.isAvailable(copy));
        
    }

    private final Book book2 = new Book("this is book2 title", Arrays.asList("shawn"), 2017);
    @Test
    public void testIsAvailable() {
        Library library = makeLibrary();
        BookCopy copy = library.buy(book1);
        assertTrue(library.isAvailable(copy));
        assertFalse("no book2", library.isAvailable(new BookCopy(book2)));
    }
    
    @Test
    public void testAllCopiesNone() {
        Library library = makeLibrary();
        assertEquals(Collections.emptySet(), library.allCopies(book1));
    }

    @Test
    public void testAllCopiesOne() {
        Library library = makeLibrary();
        BookCopy copy = library.buy(book1);
        Set<BookCopy> copies = new HashSet<>();
        copies.add(copy);
        assertEquals(copies, library.allCopies(book1));
    }
    
    @Test
    public void testAllCopiesManyBook() {
        Library library = makeLibrary();
        BookCopy copy1 = library.buy(book1);
        BookCopy copy2 = library.buy(book1);
        library.checkout(copy1);
        Set<BookCopy> copies = new HashSet<>();
        copies.add(copy1);
        copies.add(copy2);
        assertEquals(copies, library.allCopies(book1));
    }
    
    @Test
    public void testAvailableCopiesEmpty() {
        Library library = makeLibrary();
        BookCopy copy1 = library.buy(book1);
        library.checkout(copy1);
        Set<BookCopy> copies = new HashSet<>();
        assertEquals(copies, library.availableCopies(book1));
    }
    
    @Test
    public void testAvailableCopiesOne() {
        Library library = makeLibrary();
        BookCopy copy1 = library.buy(book1);
        Set<BookCopy> copies = new HashSet<>();
        copies.add(copy1);
        assertEquals(copies, library.availableCopies(book1));
    }

    private final Book book3 = new Book("Harry Porter", Arrays.asList("shawn"), 2017);
    @Test
    public void testFindEmpty() {
        Library library = makeLibrary();
        library.buy(book3);
        assertEquals(Collections.emptyList(), library.find("harry porter"));
        assertEquals(Collections.emptyList(), library.find("Shawn Tsai"));
    }

    @Test
    public void testFindAuthor() {
        Library library = makeLibrary();
        library.buy(book3);
        assertEquals(Arrays.asList(book3), library.find("shawn"));
    }
    
    @Test
    public void testFindTitle() {
        Library library = makeLibrary();
        library.buy(book3);
        assertEquals(Arrays.asList(book3), library.find("Harry Porter"));
    }
    
    private final Book book4 = new Book("Harry Porter", Arrays.asList("shawn"), 2018);
    @Test
    public void testFindSameTitleAndAuthorOrderByDate() {
        Library library = makeLibrary();
        library.buy(book3);
        library.buy(book4);
        assertEquals(2, library.find("shawn").size());
        assertEquals(Arrays.asList(book4, book3), library.find("shawn"));
    }
   
    @Test
    public void testloseCopy() {
        Library library = makeLibrary();
        BookCopy copy = library.buy(book4);
        library.lose(copy);
        assertFalse("this copy is lost", library.isAvailable(copy));
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
