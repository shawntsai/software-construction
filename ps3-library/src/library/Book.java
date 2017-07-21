package library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Book is an immutable type representing an edition of a book -- not the physical object, 
 * but the combination of words and pictures that make up a book.  Each book is uniquely
 * identified by its title, author list, and publication year.  Alphabetic case and author 
 * order are significant, so a book written by "Fred" is different than a book written by "FRED".
 */
public class Book {
    private final String title;
    private final List<String> authors;
    private final int year;

    // Rep invariant:
    //     length of title and authors > 0, 
    //     title and author in list must contain at least one non-space character
    //     year > 0
    //     author order alphabetically, not repeated
    // Abstraction function:
    //     represents book title, author list and publication year
    // Safety from rep exposure:
    //     All field are private;
    //     title and year are String and int, so are guarantee immutable
    //     authors is mutable list, so constructor authors and getAuthors must make defensive copies
    
    /**
     * Make a Book.
     * @param title Title of the book. Must contain at least one non-space character.
     * @param authors Names of the authors of the book.  Must have at least one name, and each name must contain 
     * at least one non-space character.
     * @param year Year when this edition was published in the conventional (Common Era) calendar.  Must be nonnegative. 
     */
    public Book(String title, List<String> authors, int year) {
        this.title = title;
        this.authors = new ArrayList<String>(authors);
        Collections.sort(this.authors);
        this.year = year;
        checkRep();
//        throw new RuntimeException("not implemented yet");
    }
    
    private boolean checkContainAtLeastOneNonSpace(String word) {
        if (word.length() == 0) {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != ' ') {
                return true;
            }
        }
        return false;
    }
    
    // assert the rep invariant
    private void checkRep() {
        assert title.length() > 0 && authors.size() > 0;
        assert year > 0;
        assert checkContainAtLeastOneNonSpace(title);
        for (String author: authors) {
            assert checkContainAtLeastOneNonSpace(author);
        }
    }
    
    /**
     * @return the title of this book
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * @return the authors of this book
     */
    public List<String> getAuthors() {
        checkRep();
        return new ArrayList<>(authors);
    }

    /**
     * @return the year that this book was published
     */
    public int getYear() {
        return year;
    }

    /**
     * @return human-readable representation of this book that includes its title,
     *    authors, and publication year
     */
    public String toString() {
        StringBuilder authorNames = new StringBuilder();
        for (String name: authors) {
            authorNames.append(name);
            authorNames.append(',');
        }
        return String.format("%s, %s %d", title, authorNames, year);
    }

    // uncomment the following methods if you need to implement equals and hashCode,
    // or delete them if you don't
     @Override
     public boolean equals(Object that) {
         if (! (that instanceof Book)) return false;
         Book other = (Book) that;
         return other.year == this.year
             && other.title.equals(this.title)
             && other.authors.equals(this.authors);
     }
     
     @Override
     public int hashCode() {
         int hashCode = 1;
         hashCode = hashCode * 37 + this.year;
         hashCode = hashCode * 37 + this.title.hashCode();
         hashCode = hashCode * 37 + this.authors.hashCode();
         return hashCode; 
     }



    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
