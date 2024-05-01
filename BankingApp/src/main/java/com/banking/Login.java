package com.banking;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Login")
public class Login extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Connection con = null;
		PrintWriter pw = response.getWriter();
		response.setContentType("text/html");
		try 
		{
			con = DBConnection.get();
			String user = request.getParameter("uname");
			String pwd = request.getParameter("pwd");
			
			String query = "select password from register where username=?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, user);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				if(pwd.equals(rs.getString("password")))  
				{
					HttpSession session=request.getSession();  
				    session.setAttribute("name", user);

				    pw.println("<h1 style='text-align : center'> Welcome " + user + "</h1>");
					RequestDispatcher rd=request.getRequestDispatcher("/user.html");  
			        rd.include(request, response);
			        pw.println("<center>\r\n"
			        		+ "			<img src=\"\\BankingApp\\images\\bank.png\" width=\"1000\" height=\"550\"/>\r\n"
			        		+ "		</center>");
				}
				else
				{
					pw.print("<h3 style='text-align : center'>Invalid User name and Password - Try Again</h3>");  
			        RequestDispatcher rd=request.getRequestDispatcher("/login.html");  
			        rd.include(request, response);  
				}
			}
			else
			{
				pw.print("<h3 style='text-align : center'>Invalid Record Details - Try Again</h3>");  
		        RequestDispatcher rd=request.getRequestDispatcher("/login.html");  
		        rd.include(request, response);  
			}
		}
		catch(Exception e)
		{
			pw.println("<h1>Exception : " + e.getMessage() + "</h1>");
			pw.print("<h3>Login Failed - Try Again</h3>");  
	        RequestDispatcher rd=request.getRequestDispatcher("/login.html");  
	        rd.include(request, response);
		}
		finally
		{
			if(con != null)
			{
				try {
					con.close();
				} catch (SQLException e) {}
			}
		}
	}
}


