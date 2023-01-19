package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import library.Book;
import library.Library;
import library.Section;
import library.Shelf;

public class Books extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        Library library = (Library) req.getSession().getAttribute("library");
        int numSections = 1;
        for (Section section : library.sections) {
            out.println("Section #" + numSections);
            for (Shelf shelf : section.shelves) {
                out.println(shelf.alphabet + ": ");
                for (Book book : shelf.books) {
                    out.println(book.title);
                }
            }
            numSections++;
        }
    }
}
