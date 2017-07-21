package library;

/**
 * BookCopy is a mutable type representing a particular copy of a book that is held in a library's
 * collection.
 */
public class BookCopy {
    private final Book book;
    private Condition condition;
        
    // Rep invariant:
    // book has only two condition which is good and damaged
    // 
    // Abstraction function:
    // condition represent the book's condition
    // book represent the book copy
    // Safety from rep exposure argument:
    // all variables are private; and mutable book would make defensive copies  for constructor and getBook
    
    public static enum Condition {
        GOOD, DAMAGED
    };
    
    /**
     * Make a new BookCopy, initially in good condition.
     * @param book the Book of which this is a copy
     */
    public BookCopy(Book book) {
        this.book = new Book(book.getTitle(), book.getAuthors(), book.getYear());
        this.condition = Condition.GOOD;
        checkRep();
    }
    
    public BookCopy clone() {
        BookCopy copy = new BookCopy(this.book);
        copy.setCondition(this.condition);
        return copy;
    }
    
    
    // assert the rep invariant
    private void checkRep() {
        assert this.condition == Condition.GOOD || this.condition == Condition.DAMAGED;
    }
    
    /**
     * @return the Book of which this is a copy
     */
    public Book getBook() {
        return new Book(this.book.getTitle(), this.book.getAuthors(), this.book.getYear());
    }
    
    /**
     * @return the condition of this book copy
     */
    public Condition getCondition() {
        return this.condition;
    }

    /**
     * Set the condition of a book copy.  This typically happens when a book copy is returned and a librarian inspects it.
     * @param condition the latest condition of the book copy
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
        checkRep();
    }
    
    /**
     * @return human-readable representation of this book that includes book.toString()
     *    and the words "good" or "damaged" depending on its condition
     */
    public String toString() {
        String bookCondition = condition.equals(Condition.GOOD) ? "good" : "damaged";
        return String.format("%s condition: %s", book.toString(), bookCondition);
    }

//     uncomment the following methods if you need to implement equals and hashCode,
//     or delete them if you don't
     @Override
     public boolean equals(Object that) {
         if (! (that instanceof BookCopy)) return false;
         BookCopy other = (BookCopy) that;
         if (other == this) {
             return true;
         }
         else return false;
     }
     
     @Override
     public int hashCode() {
         int hashCode = 1;
         hashCode = hashCode * 37 + this.book.hashCode();
         hashCode = hashCode * 37 + this.condition.hashCode();
         return hashCode;
     }


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
