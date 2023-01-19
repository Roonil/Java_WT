package library;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.sql.*;
import dbConn.Connectors;

import javax.naming.NameNotFoundException;
import javax.naming.SizeLimitExceededException;

public class Main {

    static Library library;

    public static void initialiseLibrary(int numSections, int shelfBookCapacity, double penalty)
            throws SizeLimitExceededException, NameNotFoundException, SQLException, ClassNotFoundException {
        library = new Library(numSections, shelfBookCapacity, penalty);
        Connectors.tableAddLibrary(library);
    }

    public static void displayCard(int userID) {
        library.users.get(userID - 1).displayCard();
    }

    public static void addBook(Book book) {
        try {
            library.addBook(book, false);
            Connectors.tableAddBook(book, false);
        } catch (SizeLimitExceededException e) {
            System.out.println(e.getMessage());
        } catch (NameNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addBorrower(String name, String address) {
        try {
            library.addBorrower(name, address);
            Connectors.tableAddBorrower(library.users.get(library.users.size() - 1));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void returnBook(Book book, int userID) {
        try {

            for (Borrower borrower : library.users) {
                if (borrower.userID == userID) {
                    LibraryCardRecord rec = borrower.returnBook(book.id, book.title,
                            DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now()));
                    library.addBook(book, true);
                    Connectors.tableAddBook(book, true);
                    Connectors.tableAddRecord(userID, rec, true);
                }
            }

        } catch (NameNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SizeLimitExceededException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeBook(Book book) {
        try {
            library.removeBook(-1, book.id,
                    book.title, false, null);

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void checkOut(int userID, int bookID, String bookTitle, String dateTo) {
        try {
            library.removeBook(userID, bookID,
                    bookTitle, true, dateTo);
            Connectors.tableRemoveBook(bookID, true);
            Connectors.tableAddRecord(userID, library.users.get(userID - 1).libraryCard.records
                    .get(library.users.get(userID - 1).libraryCard.records.size() - 1),
                    false);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws SQLException, SizeLimitExceededException, NameNotFoundException {
        library = Connectors.tableGetLibrary();
        // library.getDetails();
        // initialiseLibrary(2, 4, 0.0);
        // addBook(new Book(
        // "Harry Potter And The Half-Blood Prince", "J.K. Rowling", "Fiction", "Chapter
        // 1", 1001, 100));
        // addBorrower("Anand Verma", "4 Privet Drive");

        // checkOut(1, 1001, "Harry Potter And The Half-Blood Prince",
        // DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.of(2023, 10,
        // 01)));
        displayCard(1);
        // returnBook(new Book(
        // "Harry Potter And The Half-Blood Prince", "J.K. Rowling", "Fiction", "Chapter
        // 1", 1001, 100), 1);

        // returnBook(new Book(
        // "Harry Potter And The Half-Blood Prince", "J.K. Rowling", "Fiction", "Chapter
        // 1", 1001, 100.00), 1);

        // displayCard(1);
        library.getDetails();
    }

}
