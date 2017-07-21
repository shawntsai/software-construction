package library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 
 * SmallLibrary represents a small collection of books, like a single person's home collection.
 */
public class SmallLibrary implements Library {

    // This rep is required! 
    // Do not change the types of inLibrary or checkedOut, 
    // and don't add or remove any other fields.
    // (BigLibrary is where you can create your own rep for
    // a Library implementation.)

    // rep
    private Set<BookCopy> inLibrary;
    private Set<BookCopy> checkedOut;
    
    // rep invariant:
    //    the intersection of inLibrary and checkedOut is the empty set
    //
    // abstraction function:
    //    represents the collection of books inLibrary union checkedOut,
    //      where if a book copy is in inLibrary then it is available,
    //      and if a copy is in checkedOut then it is checked out

    // TODO: safety from rep exposure argument
    
    public SmallLibrary() {
        inLibrary = new HashSet<>();
        checkedOut = new HashSet<>();
        checkRep();
    } 
    
    // assert the rep invariant
    private void checkRep() {
        Set<BookCopy> intersection = new HashSet<>(inLibrary);
        intersection.retainAll(checkedOut);
        assert(intersection.isEmpty());
    }

    @Override
    public BookCopy buy(Book book) {
        BookCopy bookCopy = new BookCopy(book);
        inLibrary.add(bookCopy);
        checkRep();
        return bookCopy;
    }
    
    @Override
    public void checkout(BookCopy copy) {
       if (!inLibrary.remove(copy)) {
            throw new RuntimeException("no copy in this library"); 
        }
        checkedOut.add(copy);

        checkRep();
    }
    
    @Override
    public void checkin(BookCopy copy) {
        if (!checkedOut.remove(copy)){
            throw new RuntimeException("this book has not checkedOut");
        }
        inLibrary.add(copy);
        checkRep();
    }
    
    @Override
    public boolean isAvailable(BookCopy copy) {
        return inLibrary.contains(copy);
    }
    
    @Override
    public Set<BookCopy> allCopies(Book book) {
        Set<BookCopy> copies = new HashSet<>();
        Set<BookCopy> unionAllBook = new HashSet<>(inLibrary);
        unionAllBook.addAll(checkedOut);
        for (BookCopy copy: unionAllBook) {
            if (copy.getBook().equals(book)) {
                copies.add(copy);
            }
        }
        checkRep();
        return copies;
    }
    
    @Override
    public Set<BookCopy> availableCopies(Book book) {
        Set<BookCopy> copies = new HashSet<>();
        for (BookCopy copy: inLibrary) {
            if (copy.getBook().equals(book)) {
                copies.add(copy);
            }
        }
        checkRep();
        return copies;
    }

    @Override
    public List<Book> find(String query) {
        Set<BookCopy> unionAllBook = new HashSet<>(inLibrary);
        Set<Book> allBooks = new HashSet<>();
        for (BookCopy copy: unionAllBook) {
            allBooks.add(copy.getBook());
        }
        unionAllBook.addAll(checkedOut);
        List<Book> foundMatched = new ArrayList<>();
        for (Book book: allBooks) {
            if (book.getTitle().equals(query)) {
                foundMatched.add(book);
                continue;
            }
            for (String author : book.getAuthors()) {
                if (query.equals(author)) {
                    foundMatched.add(book);
                    continue;
                }
            }
        }
        Collections.sort(foundMatched, new Comparator <Book>() {
            @Override
            public int compare(Book a, Book b) {
                if (a.getTitle().equals(b.getTitle()) && a.getAuthors().equals(b.getAuthors())) {
                    return b.getYear() - a.getYear(); 
                }
                // TODO: not implement if found matched which is not all matched 
                // for example jessy young yang with search key jessy
                else return a.getTitle().compareTo(b.getTitle());
            }
        });
        checkRep();
        return foundMatched;
    }
    
    @Override
    public void lose(BookCopy copy) {
        if (inLibrary.remove(copy) || checkedOut.remove(copy)) {
            return;
        }
        checkRep();
        throw new RuntimeException("this copy is already lost");
    }

    // uncomment the following methods if you need to implement equals and hashCode,
    // or delete them if you don't
    // @Override
    // public boolean equals(Object that) {
    //     throw new RuntimeException("not implemented yet");
    // }
    // 
    // @Override
    // public int hashCode() {
    //     throw new RuntimeException("not implemented yet");
    // }
    

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
