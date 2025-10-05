package com.UI.pages;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class AdminPage extends HttpServlet {

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
            out.println("<html><head><title>ADMINSTORE</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background: #f5f5f5; }");
            out.println("h2 { text-align: center; }");
            out.println("table { width: 80%; margin: auto; border-collapse: collapse; background: white; }");
            out.println("th, td { padding: 12px; text-align: center; border: 1px solid #ddd; }");
            out.println("th { background: #007BFF; color: white; }");
            out.println("tr:nth-child(even) { background: #f9f9f9; }");
            out.println("form { margin: 0; }");
            out.println(".btn { background: #28a745; color: white; padding: 6px 12px; border: none; cursor: pointer; border-radius: 4px; }");
            out.println(".btn:hover { background: #218838; }");
            out.println("</style></head><body>");

            out.println("<h2>Welcome to the Store</h2>");
            out.println("<a href='cart'>View Cart</a><br><br>");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM Product LIMIT 5");

            out.println("<table>");
            out.println("<tr><th>ID</th><th>Name</th><th>Price</th><th>Action</th></tr>");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String price = rs.getBigDecimal("price").toString();

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("<td>$" + price + "</td>");
                out.println("<td>");
                out.println("<form method='post' action='cart'>");
                out.println("<input type='hidden' name='id' value='" + id + "'/>");
                out.println("<input type='hidden' name='name' value='" + name + "'/>");
                out.println("<input type='hidden' name='price' value='" + price + "'/>");
                out.println("<input type='hidden' name='action' value='add'/>");
                out.println("<input type='submit' class='btn' value='Add to Cart'/>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h2>Connection failed</h2>");
            out.println("<pre>" + e.getMessage() + "</pre>");
        }
    }
}
