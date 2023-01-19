package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NameNotFoundException;
import javax.naming.SizeLimitExceededException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dbConn.Connectors;
import library.Library;

public class Index extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        try {
            Library library = Connectors.tableGetLibrary();
            session.setAttribute("library", library);
            resp.sendRedirect(req.getContextPath() + "/books");
        } catch (SizeLimitExceededException | NameNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO: Implement IDs for library (The table)

    }
}
