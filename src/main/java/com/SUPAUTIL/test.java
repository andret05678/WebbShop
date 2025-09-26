package com.SUPAUTIL;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class test extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        // Explicitly load the PostgreSQL driver
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
            ResultSet rs = stmt.executeQuery("SELECT NOW()");
            if (rs.next()) {
                out.println("<p>DB time: " + rs.getString(1) + "</p>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h2>Connection failed</h2>");
            out.println("<pre>" + e.getMessage() + "</pre>");
        }
    }
}