package com.UI;

import com.UI.Info.UserInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/testpage")
public class TestPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = req.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

        if (userInfo == null) {
            resp.sendRedirect("login");
            return;
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://aws-1-eu-north-1.pooler.supabase.com:5432/postgres?user=postgres.yibhllavyovhbjaxwynu&password=Anton056780990";
            conn = DriverManager.getConnection(url);

            out.println("<html><head><title>Store</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background: #f5f5f5; margin: 0; padding: 20px; }");
            out.println(".header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; padding: 10px; background: white; border-radius: 8px; }");
            out.println("h2 { text-align: center; color: #333; }");
            out.println("table { width: 80%; margin: 20px auto; border-collapse: collapse; background: white; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
            out.println("th, td { padding: 12px; text-align: center; border: 1px solid #ddd; }");
            out.println("th { background: #007BFF; color: white; }");
            out.println("tr:nth-child(even) { background: #f9f9f9; }");
            out.println("form { margin: 0; }");
            out.println(".btn { background: #28a745; color: white; padding: 8px 16px; border: none; cursor: pointer; border-radius: 4px; text-decoration: none; display: inline-block; }");
            out.println(".btn:hover { background: #218838; }");
            out.println(".btn-cart { background: #6c757d; }");
            out.println(".btn-cart:hover { background: #545b62; }");
            out.println(".btn-logout { background: #dc3545; }");
            out.println(".btn-logout:hover { background: #c82333; }");
            out.println(".btn-admin { background: #fd7e14; }");
            out.println(".btn-admin:hover { background: #e96b00; }");
            out.println(".user-info { background: #e9ecef; padding: 10px; border-radius: 4px; margin-bottom: 10px; }");
            out.println("</style></head><body>");

            // Header with user info
            out.println("<div class='header'>");
            out.println("<h1>Welcome to the Store</h1>");
            out.println("<div>");
            out.println("<span class='user-info'>");
            out.println("Welcome, " + userInfo.getUsername() + " (");

            String role = "";
            switch(userInfo.getRole()) {
                case 1: role = "Customer"; break;
                case 2: role = "Staff"; break;
                case 3: role = "Admin"; break;
            }
            out.println(role + ")");
            out.println("</span> ");
            out.println("<a href='cart' class='btn btn-cart'>View Cart</a> ");

            if (userInfo.getRole() == 3) {
                out.println("<a href='admin' class='btn btn-admin'>Admin Panel</a> ");
            }

            out.println("<a href='logout' class='btn btn-logout'>Logout</a>");
            out.println("</div>");
            out.println("</div>");

            // Get products from database
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id, name, price FROM Product LIMIT 5");

            out.println("<h2>Available Products</h2>");
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

        } catch (ClassNotFoundException e) {
            out.println("<h2>PostgreSQL Driver not found</h2>");
            out.println("<pre>" + e.getMessage() + "</pre>");
            e.printStackTrace();
        } catch (SQLException e) {
            out.println("<h2>Database Connection Failed</h2>");
            out.println("<pre>" + e.getMessage() + "</pre>");
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // If you need to handle POST requests in the future
        doGet(req, resp);
    }
}