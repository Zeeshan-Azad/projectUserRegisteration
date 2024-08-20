package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FindUserServlet")
public class FindUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String jsonResponse = "";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
       
              Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/RegisterationDB", "root", "tiger");
             PreparedStatement ps = con.prepareStatement("SELECT username, email FROM users WHERE email = ?");

            

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // User found
                    JSONObject userJson = new JSONObject();
                    userJson.put("username", rs.getString("username"));
                    userJson.put("email", rs.getString("email"));
                    jsonResponse = userJson.toString();
                    response.setStatus(HttpServletResponse.SC_OK); // HTTP 200 OK
                } else {
                    // User not found
                    jsonResponse = new JSONObject().put("message", "User not found.").toString();
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND); // HTTP 404 Not Found
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse = new JSONObject().put("message", "An error occurred while processing your request.").toString();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // HTTP 500 Internal Server Error
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
}
