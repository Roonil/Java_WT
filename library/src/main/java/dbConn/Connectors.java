package dbConn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.naming.NameNotFoundException;
import javax.naming.SizeLimitExceededException;

import library.*;

public class Connectors {
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/library?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String DB_USER = "root";
    static final String DB_PASS = "password";

    static void tableGetBooks(Library library) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            if (tableExists(conn, "Books")) {
                String query = "Select * from Books";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    library.addBook(new Book(rs.getString("title"), rs.getString("author"), rs.getString("genre"),
                            rs.getString("contents"), rs.getInt("id"), rs.getDouble("price"),
                            rs.getBoolean("isAvailable")),
                            false);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SizeLimitExceededException e) {
            System.out.println(e.getMessage());
        } catch (NameNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void tableRemoveBook(int bookID, boolean isCheckOut) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            if (tableExists(conn, "Books") && isCheckOut == false) {
                String query = "DELETE TOP(1) From Books where id=?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, bookID);
                preparedStatement.executeUpdate();
            } else if (tableExists(conn, "Books") && isCheckOut == true) {
                String query = "UPDATE Books SET isAvailable=? where id=? and isAvailable=? limit 1";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setBoolean(1, !isCheckOut);
                preparedStatement.setInt(2, bookID);
                preparedStatement.setBoolean(3, isCheckOut);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void tableAddRecord(final int userID, final LibraryCardRecord libraryCardRecord,
            final boolean returnBook) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            if (!tableExists(conn, "LibraryCardRecords")) {
                String query = "CREATE TABLE LibraryCardRecords(libraryCardID INTEGER NOT NULL, bookID INTEGER NOT NULL, bookTitle varchar(255) NOT NULL, dateFrom varchar(255) NOT NULL,dateTo varchar(255) NOT NULL, returnDate varchar(255) , hasReturned BIT NOT NULL)";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
            }
            if (!returnBook) {
                String query = "INSERT INTO LibraryCardRecords VALUES(?,?,?,?,?,NULL,0)";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, libraryCardRecord.libraryCardID);
                preparedStatement.setInt(2, libraryCardRecord.bookID);
                preparedStatement.setString(3, libraryCardRecord.bookTitle);
                preparedStatement.setString(4, libraryCardRecord.dateFrom.toString());
                preparedStatement.setString(5, libraryCardRecord.dateTo.toString());
                preparedStatement.executeUpdate();
            } else {
                String query = "UPDATE LibraryCardRecords SET returnDate=?, hasReturned=1 where libraryCardID=? and bookID=? and hasReturned=0 limit 1";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, libraryCardRecord.returnDate.toString());
                preparedStatement.setInt(2, libraryCardRecord.libraryCardID);
                preparedStatement.setInt(3, libraryCardRecord.bookID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void tableAddLibraryCard(LibraryCard libraryCard) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            if (!tableExists(conn, "LibraryCards")) {
                String query = "CREATE TABLE LibraryCards(userID INTEGER not NULL UNIQUE, libraryCardID INTEGER PRIMARY KEY, issueDate VARCHAR(255) not NULL)";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
            }
            String query = "INSERT INTO LibraryCards VALUES(?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, libraryCard.userID);
            preparedStatement.setInt(2, libraryCard.libraryCardID);
            preparedStatement.setString(3, libraryCard.issueDate.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void tableAddBorrower(final Borrower borrower) {
        try {
            final Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            if (!tableExists(conn, "Borrowers")) {
                final String query = "CREATE TABLE Borrowers(name varchar(255) not NULL, address varchar(255) not NULL, userID INTEGER PRIMARY KEY, libraryCardID INTEGER not NULL)";
                final Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
            }

            final String query = "INSERT INTO Borrowers Values(?,?,?,?)";
            final PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, borrower.name);
            preparedStatement.setString(2, borrower.address);
            preparedStatement.setInt(3, borrower.userID);
            preparedStatement.setInt(4, borrower.libraryCard.libraryCardID);
            preparedStatement.executeUpdate();
            tableAddLibraryCard(borrower.libraryCard);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void tableAddBook(Book book, boolean returnBook) {
        try {
            final Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            if (!tableExists(conn, "Books")) {
                String query = "Create Table Books(id INTEGER not NULL, title varchar(255) not NULL, author varchar(255) not NULL, contents varchar(255) not NULL, genre varchar(255) not NULL, price double(5,2) not NULL, isAvailable BIT not NULL )";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
            }
            if (tableExists(conn, "Books") && returnBook == false) {
                final String query = "INSERT INTO Books VALUES(?,?,?,?,?,?,?)";
                final PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, book.id);
                preparedStatement.setString(2, book.title);
                preparedStatement.setString(3, book.author);
                preparedStatement.setString(4, book.contents);
                preparedStatement.setString(5, book.genre);
                preparedStatement.setDouble(6, Double.parseDouble(String.format("%.2f", book.price)));
                preparedStatement.setBoolean(7, book.isAvailable);
                preparedStatement.executeUpdate();
            } else if (tableExists(conn, "Books") && returnBook == true) {
                final String query = "UPDATE Books SET isAvailable=? where id=? and isAvailable=? limit 1";
                final PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setBoolean(1, returnBook);
                preparedStatement.setInt(2, book.id);
                preparedStatement.setBoolean(3, !returnBook);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    static boolean tableExists(Connection connection, String tableName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) "
                + "FROM information_schema.tables "
                + "WHERE table_name = ?"
                + "LIMIT 1;");
        preparedStatement.setString(1, tableName);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) != 0;
    }

    public static ArrayList<LibraryCardRecord> tableGetRecords(int libraryCardID) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        if (tableExists(conn, "LibraryCards")) {
            String query = "Select * from LibraryCardRecords where libraryCardID=?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, libraryCardID);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<LibraryCardRecord> records = new ArrayList<LibraryCardRecord>();
            while (rs.next()) {
                records.add(new LibraryCardRecord(rs.getString("dateFrom"), rs.getString("dateTo"), rs.getInt("bookID"),
                        libraryCardID, rs.getString("bookTitle"), rs.getBoolean("hasReturned"),
                        rs.getString("returnDate")));
            }
            return records;
        }
        return null;

    }

    public static LibraryCard tableGetLibraryCard(int libraryCardID) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        if (tableExists(conn, "LibraryCards")) {
            String query = "Select * from LibraryCards where libraryCardID=?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, libraryCardID);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<LibraryCardRecord> records = tableGetRecords(libraryCardID);
            while (rs.next()) {

                LibraryCard libraryCard = new LibraryCard(rs.getInt("userID"), rs.getInt("libraryCardID"),
                        DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now()));
                libraryCard.records.addAll(records);
                return libraryCard;
            }

        }
        return null;

    }

    public static void tableGetBorrowers(Library library) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        if (tableExists(conn, "Borrowers")) {
            String query = "Select * from Borrowers";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                LibraryCard libraryCard = tableGetLibraryCard(rs.getInt("libraryCardID"));
                library.addBorrower(rs.getString("name"), rs.getString("address"), rs.getInt("userID"),
                        libraryCard);
            }
        }
    }

    public static Library tableGetLibrary() throws SQLException, SizeLimitExceededException, NameNotFoundException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        if (tableExists(conn, "Library")) {
            Statement stmt = conn.createStatement();
            String sql = "Select * from Library limit 1";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Library library = new Library(rs.getInt("numSections"), rs.getInt("shelfBookCapacity"),
                        rs.getDouble("penalty"));
                tableGetBooks(library);
                tableGetBorrowers(library);
                return library;
            }

        }
        return null;
    }

    public static void tableAddLibrary(Library library) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        Statement stmt = conn.createStatement();
        if (!tableExists(conn, "Library")) {
            String query = "CREATE TABLE Library (numSections INTEGER not NULL, shelfBookCapacity INTEGER not NULL, penalty double(5,2) not NULL)";
            stmt.executeUpdate(query);
        }
        String query = "INSERT INTO Library Values(" + library.numSections + " , " + library.shelfBookCapacity + " , "
                + library.penalty + ")";
        stmt.executeUpdate(query);
    }

}
