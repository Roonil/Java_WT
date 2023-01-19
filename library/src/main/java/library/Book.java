package library;

public class Book {
    public final String title, author, genre, contents;
    public final int id;
    public final double price;
    public boolean isAvailable = true;

    Book(String title, String author, String genre, String contents, int id, double price) {
        this.title = title;
        this.author = author;
        this.contents = contents;
        this.genre = genre;
        this.id = id;
        this.price = price;
    }

    public Book(String title, String author, String genre, String contents, int id, double price, boolean isAvailable) {
        this.title = title;
        this.author = author;
        this.contents = contents;
        this.genre = genre;
        this.id = id;
        this.price = price;
        this.isAvailable = isAvailable;
    }
}
