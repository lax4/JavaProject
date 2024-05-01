package com.banking;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Withdraw")
public class Withdraw extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        response.setContentType("text/html");
        HttpSession session = request.getSession(false);
        if (session != null) {
            String user = (String) session.getAttribute("name");
            pw.print("<h1 align='center'>Welcome, " + user + ". Continue with your transactions</h1>");
            Connection con = null;
            try {
                con = DBConnection.get();
                int num = Integer.parseInt(request.getParameter("accountNumberWithdraw").trim());
                int amt = Integer.parseInt(request.getParameter("amountWithdraw").trim());

                // Create a CallableStatement
                CallableStatement cs = con.prepareCall("{CALL withdraw_funds(?, ?, ?)}");
                cs.setInt(1, num);
                cs.setInt(2, amt);
                cs.registerOutParameter(3, Types.INTEGER); // Success indicator
                cs.execute();
                int success = cs.getInt(3);

                if (success == 1) {
                    pw.print("<h3 align='center'>Withdrawal Successful</h3>");
                } else if (success == 0) {
                    pw.print("<h3 align='center'>Insufficient Balance in Account</h3>");
                } else {
                    pw.print("<h3 align='center'>Invalid Account Number Given</h3>");
                }
                RequestDispatcher rd=request.getRequestDispatcher("/user.html");  
			    rd.include(request, response);
                cs.close(); 
            } 
            catch (NumberFormatException | SQLException e) {
                pw.print("<h3 align='center'>Invalid Account Number or Amount - Try Again</h3>");
                RequestDispatcher rd=request.getRequestDispatcher("/user.html");  
			    rd.include(request, response);
            } 
            finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        // Log or handle the error
                    }
                }
            }
        } 
        else {
            pw.print("<h3>You logged out from the previous session. Please login again.</h3>");
            RequestDispatcher rd=request.getRequestDispatcher("/login.html");  
		    rd.forward(request, response);
        }
    }
}
