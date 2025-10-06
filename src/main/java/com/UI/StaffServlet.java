package com.UI;

import com.UI.Info.UserInfo;
import com.UI.Info.OrderInfo;
import com.UI.Info.ProductInfo;
import com.DB.imp.OrderDbImp;
import com.DB.imp.ProductDbImp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/staffpage")
public class StaffServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in and is staff (role 2) or admin (role 3)
        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

        if (userInfo == null) {
            response.sendRedirect("login");
            return;
        }

        if (userInfo.getRole() != 2 && userInfo.getRole() != 3) {
            response.sendRedirect("testpage");
            return;
        }

        String tab = request.getParameter("tab");
        if (tab == null) tab = "orders";

        try {
            switch (tab) {
                case "orders":
                    List<OrderInfo> allOrders = OrderDbImp.getAllOrders();
                    request.setAttribute("allOrders", allOrders);
                    break;
                case "products":
                    List<ProductInfo> allProducts = ProductDbImp.getAllProducts();
                    request.setAttribute("allProducts", allProducts);
                    break;
                case "inventory":
                    List<ProductInfo> lowStockProducts = ProductDbImp.getLowStockProducts();
                    request.setAttribute("lowStockProducts", lowStockProducts);
                    break;
            }

            request.setAttribute("currentTab", tab);
            request.getRequestDispatcher("/staffpage.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("staffpage?error=Unable to load data");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is staff or admin
        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

        if (userInfo == null || (userInfo.getRole() != 2 && userInfo.getRole() != 3)) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        String tab = request.getParameter("tab");
        if (tab == null) tab = "orders";

        try {
            switch (action) {
                case "updateOrderStatus":
                    handleUpdateOrderStatus(request, response);
                    break;
                case "updateProductStock":
                    handleUpdateProductStock(request, response);
                    break;
                case "updateProduct":
                    handleUpdateProduct(request, response);
                    break;
                default:
                    response.sendRedirect("staffpage?tab=" + tab + "&error=Invalid action");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("staffpage?tab=" + tab + "&error=Operation failed");
        }
    }

    private void handleUpdateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String newStatus = request.getParameter("newStatus");

        try {
            boolean success = OrderDbImp.updateOrderStatus(orderId, newStatus);

            if (success) {
                response.sendRedirect("staffpage?tab=orders&success=Order status updated successfully");
            } else {
                response.sendRedirect("staffpage?tab=orders&error=Failed to update order status");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("staffpage?tab=orders&error=Database error");
        }
    }

    private void handleUpdateProductStock(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int newStock = Integer.parseInt(request.getParameter("newStock"));

        try {
            boolean success = ProductDbImp.updateProductStock(productId, newStock);

            if (success) {
                response.sendRedirect("staffpage?tab=products&success=Product stock updated successfully");
            } else {
                response.sendRedirect("staffpage?tab=products&error=Failed to update product stock");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("staffpage?tab=products&error=Database error");
        }
    }

    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        try {
            boolean success = ProductDbImp.updateProductBasicInfo(productId, name, description, price, stock);

            if (success) {
                response.sendRedirect("staffpage?tab=products&success=Product updated successfully");
            } else {
                response.sendRedirect("staffpage?tab=products&error=Failed to update product");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("staffpage?tab=products&error=Database error");
        }
    }
}