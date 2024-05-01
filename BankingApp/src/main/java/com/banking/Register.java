package com.banking;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Register")
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Connection con = null;
        try {
            con = DBConnection.get();
            String user = request.getParameter("uname");
            String pwd = request.getParameter("pwd");

            String query = "INSERT INTO register VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, user);
                ps.setString(2, pwd);
                int count = ps.executeUpdate();
                if (count > 0) {
                    out.println("<h3 style='text-align:center'>Successfully Registered - Login Now</h3>");
                    RequestDispatcher rd = request.getRequestDispatcher("/login.html");
                    rd.include(request, response);
                } else {
                    out.println("<h3 style='text-align:center'>Registration Failed - Try Again</h3>");
                    RequestDispatcher rd = request.getRequestDispatcher("/register.html");
                    rd.include(request, response);
                }
            }
        } catch (SQLException e) {
            out.println("<h3 style='text-align:center'>Database Error: " + e.getMessage() + "</h3>");
            out.println("<h3 style='text-align:center'>Registration Failed - Try Again</h3>");
            RequestDispatcher rd = request.getRequestDispatcher("/register.html");
            rd.include(request, response);
        } catch (Exception e) {
            out.println("<h3 style='text-align:center'>Exception: " + e.getMessage() + "</h3>");
            out.println("<h3 style='text-align:center'>Registration Failed - Try Again</h3>");
            RequestDispatcher rd = request.getRequestDispatcher("/register.html");
            rd.include(request, response);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    // Handle error
                }
            }
        }
    }
}
