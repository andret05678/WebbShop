package com.UI;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class CartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
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
        }

        session.setAttribute("cart", cart);
        resp.sendRedirect("cart");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();
        List<Map<String, String>> cart = (List<Map<String, String>>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        out.println("<html><head><title>Shopping Cart</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background: #f5f5f5; }");
        out.println("h2 { text-align: center; }");
        out.println("table { width: 70%; margin: auto; border-collapse: collapse; background: white; }");
        out.println("th, td { padding: 12px; text-align: center; border: 1px solid #ddd; }");
        out.println("th { background: #28a745; color: white; }");
        out.println("tr:nth-child(even) { background: #f9f9f9; }");
        out.println(".btn-remove { background: #dc3545; color: white; padding: 6px 12px; border: none; cursor: pointer; border-radius: 4px; }");
        out.println(".btn-remove:hover { background: #c82333; }");
        out.println("</style></head><body>");

        out.println("<h2>Your Cart</h2>");
        out.println("<a href='testpage'>Back to Store</a><br><br>");

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
                out.println("<form method='post' action='cart'>");
                out.println("<input type='hidden' name='id' value='" + item.get("id") + "'/>");
                out.println("<input type='hidden' name='action' value='remove'/>");
                out.println("<input type='submit' class='btn-remove' value='Remove'/>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("<tr><td colspan='2'>Total</td><td colspan='2'>$" + total + "</td></tr>");
            out.println("</table>");
        }

        out.println("</body></html>");
    }
}
