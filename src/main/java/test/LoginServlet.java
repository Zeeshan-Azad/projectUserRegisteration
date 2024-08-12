package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		try {
			//Load the Mysql Jdbc driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//Establish connection
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/RegisterationDB", "root", "tiger");
			
			//prepare the sql querry to use
			
			String query = "Select * from users where username = ? AND password = ?";
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			PrintWriter out = response.getWriter();
            if (rs.next()) {
                response.sendRedirect("homepage.html");
            } else {
                response.sendRedirect("login.html");
                request.setAttribute("errorMessage", "Invalid username or password.");
            }
            
            rs.close();
            ps.close();
            con.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}
