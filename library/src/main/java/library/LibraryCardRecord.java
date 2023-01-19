package library;

import java.util.NoSuchElementException;

public class LibraryCardRecord {
    public final String dateFrom, dateTo, bookTitle;
    public final int bookID, libraryCardID;
    public String returnDate;
    public boolean hasReturned = false;

    LibraryCardRecord(String dateFrom, String dateTo, int bookID, int libraryCardID, String bookTitle) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.bookID = bookID;
        this.libraryCardID = libraryCardID;
        this.bookTitle = bookTitle;

    }

    public LibraryCardRecord(String dateFrom, String dateTo, int bookID, int libraryCardID, String bookTitle,
            boolean hasReturned, String returnDate) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.bookID = bookID;
        this.libraryCardID = libraryCardID;
        this.bookTitle = bookTitle;
        this.hasReturned = hasReturned;
        this.returnDate = returnDate;
    }

    LibraryCardRecord returnBook(int bookID, String bookTitle, String returnDate) throws NoSuchElementException {
        if (this.bookID == bookID && this.bookTitle.equals(bookTitle)) {
            hasReturned = true;
            this.returnDate = returnDate;
            return this;
        } else
            throw new NoSuchElementException("The specified book does not exist in these Records!");
    }
}