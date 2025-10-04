package com.SUPAUTIL;

import com.BO.Services.UserServices;
import com.BO.User;
import com.DB.imp.ProductDbImp;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class TestFetch extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            out.println("<h2>PostgreSQL Driver not found</h2>");
            out.println("<pre>" + e.getMessage() + "</pre>");
            return;
        }

        String url = "jdbc:postgresql://aws-1-eu-north-1.pooler.supabase.com:5432/postgres?user=postgres.yibhllavyovhbjaxwynu&password=Anton056780990";

        try (Connection conn = DriverManager.getConnection(url)) {
            out.println("<h2>Connected to Supabase successfully!</h2>");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM Product LIMIT 5");
            out.println("<h3>Products:</h3>");
            out.println("<ul>");
            while (rs.next()) {
                out.println("<li>ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Price: $" + rs.getBigDecimal("price") + "</li>");
            }
            // Testar procuct fetching
            out.println("<p>ID: " + ProductDbImp.findById(2).toString() + "</p>");



            UserServices userServices = new UserServices();
            String testEmail = "Felix@hotmail.com";
            String testPassword = "Felix2";
            int testRoleId = 1;

            User regUser= userServices.register(testEmail, testPassword, "Sven", testRoleId);
            if (regUser == null) {
                out.println("<p>Registration failed</p>");
            }


            User loginUser = userServices.login(testEmail, testPassword);

            if (loginUser != null) {
                req.getSession().setAttribute("token", loginUser.getToken());
                req.getSession().setAttribute("userId", loginUser.getId());

                out.println("<p> Login successful!</p>");
                out.println("<p>User: " + loginUser.getUsername() + "</p>");
                out.println("<p>Email: " + loginUser.getEmail() + "</p>");
                out.println("<p>Token: " + loginUser.getToken() + "</p>");
            } else {
                out.println("<p> Login failed (check email/password or token generation)</p>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h2>Connection failed</h2>");
            out.println("<pre>" + e.getMessage() + "</pre>");
        }
    }
}