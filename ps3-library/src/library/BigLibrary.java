package library;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * BigLibrary represents a large collection of books that might be held by a city or
 * university library system -- millions of books.
 * 
 * In particular, every operation needs to run faster than linear time (as a function of the number of books
 * in the library).
 */
public class BigLibrary implements Library {

    // TODO: rep
    final private Map<String, Set<Book>> keyToBooks;
    final private Map<Book, Set<BookCopy>> bookToCopy;
    final private Map<BookCopy, CopyCondition> bookCopyState;
    
    // rep invariant:
    // bookToCopy's copy condition is the same of bookCopyState
    // TODO: abstraction function
    // TODO: safety from rep exposure argument

    public static enum CopyCondition{
        IN, OUT, LOST 
    };     

    public BigLibrary() {
        bookToCopy = new HashMap<>();
        bookCopyState = new HashMap<>();
        keyToBooks = new HashMap<>();
    }
    
    // assert the rep invariant
    private void checkRep() {
        for (BookCopy copy: bookCopyState.keySet()) {
            assert(bookToCopy.get(copy.getBook()).contains(copy));
            assert(bookCopyState.get(copy).equals(CopyCondition.IN) ||
                   bookCopyState.get(copy).equals(CopyCondition.OUT) ||
                   bookCopyState.get(copy).equals(CopyCondition.LOST));
        }
    }
    
    private void insertKeyToBook(String key, Book book) {
        if (keyToBooks.containsKey(key)) {
            keyToBooks.get(key).add(book);
        }
        else {
            Set<Book> books = new HashSet<>();
            books.add(book);
            keyToBooks.put(key, books);
        }
        checkRep();
    }
    
    private void splitWordToInsert(String word, Book book) {
        insertKeyToBook(word, book);
        String[] words = word.split(" ");
        if (words.length > 1) {
            for (String key: words) {
                insertKeyToBook(key, book);
            }
        }
    }

    @Override
    public BookCopy buy(Book book) {
        splitWordToInsert(book.getTitle(), book);
        for (String author: book.getAuthors()) {
            splitWordToInsert(author, book);
        }
        
        BookCopy copy = new BookCopy(book);
        bookCopyState.put(copy, CopyCondition.IN);
        if (bookToCopy.containsKey(book)) {
            bookToCopy.get(book).add(copy);
        }
        else {
            Set<BookCopy> copies = new HashSet<>();
            copies.add(copy);
            bookToCopy.put(book, copies);
        }
        checkRep();
        return copy;
    }
    
    @Override
    public void checkout(BookCopy copy) {
        if (!bookCopyState.containsKey(copy)) {
            throw new RuntimeException("no copy in this library"); 
        }
        if (!bookCopyState.get(copy).equals(CopyCondition.IN)) {
            throw new RuntimeException("copy lost or already checkout");
        }
        bookCopyState.put(copy, CopyCondition.OUT);
        checkRep();
    }
    
    @Override
    public void checkin(BookCopy copy) {
        if (!bookCopyState.containsKey(copy)) {
            throw new RuntimeException("no copy in this library");
        }
        if (!bookCopyState.get(copy).equals(CopyCondition.OUT)) {
            throw new RuntimeException("book should be check out first");
        }
        bookCopyState.put(copy, CopyCondition.IN);
        checkRep();
    }
    
    @Override
    public Set<BookCopy> allCopies(Book book) {
        if (bookToCopy.containsKey(book)) {
            Set<BookCopy> copies  = new HashSet<>(bookToCopy.get(book));
            for (BookCopy copy: bookToCopy.get(book)) {
                if (bookCopyState.get(copy).equals(CopyCondition.LOST)){
                   copies.remove(copy);
                }
            }
            
            return copies;
        }

        else return new HashSet<>();
    }

    @Override
    public Set<BookCopy> availableCopies(Book book) {
        Set<BookCopy> available = new HashSet<>();
        if (! bookToCopy.containsKey(book)) {
            return available;
        }
        Set<BookCopy> copies = bookToCopy.get(book);
        for (BookCopy copy: copies) {
            if (bookCopyState.get(copy).equals(CopyCondition.IN)) {
                available.add(copy);
            }
        }
        checkRep();
        return available;
    }
    
    @Override
    public boolean isAvailable(BookCopy copy) {
        if (bookCopyState.containsKey(copy))
            return bookCopyState.get(copy).equals(CopyCondition.IN);
        else return false;
    }
    
    
    private int lcs(String StrX, String StrY) {
        String[] X = StrX.split(" ");
        String[] Y = StrY.split(" ");
        int result = 0;
        int m = X.length;
        int n = Y.length;
        int[][] LCSuff = new int[m+1][n+1];
        for (int i = 0; i<= m; i++) {
            for (int j =0; j <= n; j++) {
                if (i == 0 || j== 0) {
                    LCSuff[i][j] = 0;
                }
                else if (X[i-1].equals(Y[j-1])) {
                   LCSuff[i][j] = LCSuff[i-1][j-1] + 1;
                   result = Math.max(result, LCSuff[i][j]);
                }
                else LCSuff[i][j] = 0;
            }
        }
        return result;
    }
    
    /**
     * Matching words in the keywords argument to words in title 
     * or author names.
     * Ranking the resulting list of books so that books that match more 
     * keywords appear earlier in the list.
     * Ranking books that match multiple contiguous keywords higher in the list.
     * Ranking older books or checked-out books lower in the list.
     * Supporting quotation marks in the keywords argument, so that 
     * (for example) "\"David Foster Wallace\" \"Infinite Jest\"" finds books 
     * whose title or author contains David Foster Wallace or Infinite Jest 
     * as contiguous words.
     * 
     * 
     */
    
    @Override
    public List<Book> find(String query) {
//        if (! keyToBooks.containsKey(query)) 
//            return new ArrayList<Book>();
        Map<Book, Integer> booksFrequency = new HashMap<>();
        Set<Book> books = keyToBooks.get(query);
        if (books != null) {
            for (Book book: books) {
                booksFrequency.put(book, 1);
            }
        };
        String[] queryWords = query.split(" |\"");
        for (String w: queryWords) {
            System.out.println(w);
        }
        if (queryWords.length > 1) {
            for (String w: queryWords) {
//                System.out.println(w);
//                books.addAll(keyToBooks.get(w));
                if (keyToBooks.containsKey(w)) {
                     for (Book book: keyToBooks.get(w)) {
                        booksFrequency.put(book, booksFrequency.getOrDefault(book, 0) + 1);
                    }   
                }
            }
        }
        
//        for (Book book: booksFrequency.keySet()) {
//            System.out.println(book.toString());
//        }
        
        if (booksFrequency.isEmpty()) return new ArrayList<Book>();
        
        List <Book> sortedBooks = booksFrequency.keySet().stream().sorted(new Comparator<Book>() {
            @Override 
            public int compare(Book a, Book b) {
                // check out or not
                if (! a.equals(b)) {
                    System.out.println("diffent books");
                    boolean checkoutA = isCheckout(a);
                    boolean checkoutB = isCheckout(b);
                    if (checkoutA != checkoutB) {
                        return checkoutA ? 1: -1;
                    }
                }
                if (a.getTitle().equals(b.getTitle()) && a.getAuthors().equals(b.getAuthors())) {
                    return b.getYear() - a.getYear(); 
                }
                else {
//                    System.out.println(booksFrequency.get(b));
//                    System.out.println(booksFrequency.get(a));
                    if (booksFrequency.get(b) == booksFrequency.get(a)) {
                        return lcs(b.getTitle(), query) - lcs(a.getTitle(), query);
                    }
                    else 
                        return booksFrequency.get(b) - booksFrequency.get(a);
                }
            }
        }).collect(Collectors.toList());
        
        checkRep();
        return sortedBooks;
    }
    /**
     *  
     * @param book must in library
     * @return true if one copy is available
     */
    private boolean isCheckout(Book book) {
        if (!bookToCopy.containsKey(book)) {
            throw new RuntimeException("book not in libs");
        }
        Set<BookCopy> copies = bookToCopy.get(book);
        for (BookCopy copy: copies) {
            if (bookCopyState.get(copy).equals(CopyCondition.OUT)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void lose(BookCopy copy) {
       bookCopyState.put(copy, CopyCondition.LOST);
       checkRep();
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
