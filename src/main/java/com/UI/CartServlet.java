package com.UI;

import com.BO.Services.OrderServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.sql.*;

public class CartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

        if (session.getAttribute("userInfo") == null) {
            resp.sendRedirect("login");
            return;
        }

        List<Map<String, String>> cart = (List<Map<String, String>>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        String action = req.getParameter("action");
        if ("add".equals(action)) {
            Map<String, String> item = new HashMap<>();
            item.put("id", req.getParameter("id"));
            item.put("name", req.getParameter("name"));
            item.put("price", req.getParameter("price"));
            cart.add(item);
        } else if ("remove".equals(action)) {
            String idToRemove = req.getParameter("id");
            cart.removeIf(p -> p.get("id").equals(idToRemove));
        } else if ("placeOrder".equals(action)) {
            handlePlaceOrder(req, resp, session, cart);
            return; // Return early since handlePlaceOrder handles the response
        }

        session.setAttribute("cart", cart);
        resp.sendRedirect("cart");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

        if (session.getAttribute("userInfo") == null) {
            resp.sendRedirect("login");
            return;
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        List<Map<String, String>> cart = (List<Map<String, String>>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        out.println("<html><head><title>Shopping Cart</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background: #f5f5f5; margin: 20px; }");
        out.println(".container { max-width: 900px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }");
        out.println("h2 { text-align: center; color: #333; }");
        out.println("table { width: 100%; border-collapse: collapse; background: white; margin: 20px 0; }");
        out.println("th, td { padding: 12px; text-align: center; border: 1px solid #ddd; }");
        out.println("th { background: #28a745; color: white; }");
        out.println("tr:nth-child(even) { background: #f9f9f9; }");
        out.println(".btn { padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; }");
        out.println(".btn-remove { background: #dc3545; color: white; }");
        out.println(".btn-remove:hover { background: #c82333; }");
        out.println(".btn-order { background: #007bff; color: white; font-size: 16px; padding: 12px 24px; }");
        out.println(".btn-order:hover { background: #0056b3; }");
        out.println(".btn-order:disabled { background: #6c757d; cursor: not-allowed; }");
        out.println(".success { color: #28a745; background: #d4edda; padding: 10px; border-radius: 4px; margin: 10px 0; }");
        out.println(".error { color: #dc3545; background: #f8d7da; padding: 10px; border-radius: 4px; margin: 10px 0; }");
        out.println("</style></head><body>");

        out.println("<div class='container'>");

        // Display messages from order placement
        String success = req.getParameter("success");
        String error = req.getParameter("error");
        if (success != null) {
            out.println("<div class='success'>" + success + "</div>");
        }
        if (error != null) {
            out.println("<div class='error'>" + error + "</div>");
        }

        out.println("<h2>Your Shopping Cart</h2>");

        String username = (String) session.getAttribute("username");
        if (username != null) {
            out.println("<p style='text-align:center;'>Welcome, " + username + " | ");
            out.println("<a href='logout'>Logout</a></p>");
        }

        out.println("<a href='testpage' class='btn' style='background: #6c757d; color: white;'>← Back to Store</a><br><br>");

        if (cart.isEmpty()) {
            out.println("<p style='text-align:center;'>Your cart is empty.</p>");
        } else {
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Name</th><th>Price</th><th>Action</th></tr>");
            double total = 0.0;
            for (Map<String, String> item : cart) {
                double price = Double.parseDouble(item.get("price"));
                total += price;
                out.println("<tr>");
                out.println("<td>" + item.get("id") + "</td>");
                out.println("<td>" + item.get("name") + "</td>");
                out.println("<td>$" + item.get("price") + "</td>");
                out.println("<td>");
                out.println("<form method='post' action='cart' style='display: inline;'>");
                out.println("<input type='hidden' name='id' value='" + item.get("id") + "'/>");
                out.println("<input type='hidden' name='action' value='remove'/>");
                out.println("<button type='submit' class='btn btn-remove'>Remove</button>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("<tr style='font-weight: bold;'>");
            out.println("<td colspan='2'>Total Amount</td>");
            out.println("<td>$" + String.format("%.2f", total) + "</td>");
            out.println("<td>");
            out.println("<form method='post' action='cart'>");
            out.println("<input type='hidden' name='action' value='placeOrder'/>");
            out.println("<button type='submit' class='btn btn-order'>Place Order</button>");
            out.println("</form>");
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");
        }

        out.println("</div>");
        out.println("</body></html>");
    }

    private void handlePlaceOrder(HttpServletRequest req, HttpServletResponse resp, HttpSession session, List<Map<String, String>> cart)
            throws ServletException, IOException {

        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect("cart?error=Cart is empty");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login");
            return;
        }

        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://aws-1-eu-north-1.pooler.supabase.com:5432/postgres?user=postgres.yibhllavyovhbjaxwynu&password=Anton056780990";
            conn = DriverManager.getConnection(url);

            conn.setAutoCommit(false);

            double totalAmount = 0.0;
            for (Map<String, String> item : cart) {
                totalAmount += Double.parseDouble(item.get("price"));
            }

            for (Map<String, String> item : cart) {
                int productId = Integer.parseInt(item.get("id"));
                if (!OrderServices.isProductInStock(productId)) {
                    conn.rollback();
                    resp.sendRedirect("cart?error=Product " + item.get("name") + " is out of stock");
                    return;
                }
            }

            int orderId = OrderServices.insertOrder(conn, userId, totalAmount);

            for (Map<String, String> item : cart) {
                int productId = Integer.parseInt(item.get("id"));
                double price = Double.parseDouble(item.get("price"));

                OrderServices.insertOrderItem(orderId, productId, price);

                OrderServices.updateProductStock(productId);
            }

            conn.commit();

            session.removeAttribute("cart");

            resp.sendRedirect("cart?success=Order placed successfully! Order ID: " + orderId);

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            resp.sendRedirect("cart?error=Failed to place order: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}