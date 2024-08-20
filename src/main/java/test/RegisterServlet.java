package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().print("Hello");
		//RequestDispatcher dispatcher = request.getRequestDispatcher("register.html");
	    //dispatcher.forward(request, response);
	    response.sendRedirect("register.html");
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String message = "";
        String jsonResponse = "{\"message\": \"Username already exists. Please choose a different username.\"}";
        response.setContentType("application/json");

        Connection con = null;
        PreparedStatement ps = null;
        PrintWriter out = null;
        //RequestDispatcher dispatcher = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection to the database
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/RegisterationDB", "root", "tiger");



            //prepare SQL query to check if user exists
            String checkQuery = "SELECT count(*) from users where username = ?";
            ps = con.prepareStatement(checkQuery);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if(rs.next() && rs.getInt(1) > 0) {
            	 message = "Username already exists. Please choose a different username.";
            	// Redirect to the HTML file with the message as a query parameter and use this when working with browser and UI
                 //response.sendRedirect("register.html?message=" + URLEncoder.encode(message, "UTF-8"));

            	 //error message to display on postman console as part of API calls
            	 jsonResponse = new JSONObject().put("message", "Username already exists. Please choose a different username.").toString();
            	 //jsonResponse = "{\"message\": \"Username already exists. Please choose a different username.\"}";
            	 response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
            else {
            	ps.close();
            	//message = "Please wait, Creating User";
            	// Prepare SQL query
                String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
                ps = con.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, email);

                // Execute the query
                int result = ps.executeUpdate();

                out = response.getWriter();
                if(result > 0) {
                	 jsonResponse = new JSONObject().put("message","Registration successful!").toString();
                    //out.println("Registration successful!");
                } else {
                	 jsonResponse = new JSONObject().put("message","Registration failed!").toString();
                    //out.println("Registration failed!");
                }

             // Redirect to the HTML file with the message as a query parameter
                //response.sendRedirect("register.html?message=" + URLEncoder.encode(message, "UTF-8"));


             //api endpoint work here
                response.setContentType("application/json");
                out.print(jsonResponse);
                out.flush();


                ps.close();
                con.close();

            }
            }
        catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("register.html?message=" + URLEncoder.encode("An error occurred while processing your request.", "UTF-8"));
            }


    }
}
