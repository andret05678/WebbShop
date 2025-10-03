package com.SUPAUTIL;

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

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h2>Connection failed</h2>");
            out.println("<pre>" + e.getMessage() + "</pre>");
        }
    }
}