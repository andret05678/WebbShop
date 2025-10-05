package com.UI;

import com.BO.Services.UserServices;
import com.BO.User;
import com.UI.Info.UserInfo;
import com.UI.Info.ProductInfo;
import com.UI.Info.OrderInfo;
import com.DB.imp.UserDbImp;
import com.DB.imp.ProductDbImp;
import com.DB.imp.OrderDbImp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private UserServices userServices;

    @Override
    public void init() throws ServletException {
        this.userServices = new UserServices();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is admin
        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

        if (userInfo == null || userInfo.getRole() != 3) {
            response.sendRedirect("login");
            return;
        }

        String tab = request.getParameter("tab");
        if (tab == null) tab = "users";

        try {
            switch (tab) {
                case "users":
                    List<User> allUsers = UserDbImp.getAllUsers();
                    request.setAttribute("allUsers", allUsers);
                    break;
                case "products":
                    List<ProductInfo> allProducts = ProductDbImp.getAllProducts();
                    request.setAttribute("allProducts", allProducts);
                    break;
                case "orders":
                    List<OrderInfo> allOrders = OrderDbImp.getAllOrders();
                    request.setAttribute("allOrders", allOrders);
                    break;
            }

            request.setAttribute("currentTab", tab);
            request.getRequestDispatcher("/adminpage.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin?error=Unable to load data");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is admin
        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

        if (userInfo == null || userInfo.getRole() != 3) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        String tab = request.getParameter("tab");
        if (tab == null) tab = "users";

        try {
            switch (action) {
                case "addUser":
                    handleAddUser(request, response);
                    break;
                case "editUser":
                    handleEditUser(request, response);
                    break;
                case "deleteUser":
                    handleDeleteUser(request, response);
                    break;
                case "addProduct":
                    handleAddProduct(request, response);
                    break;
                case "editProduct":
                    handleEditProduct(request, response);
                    break;
                case "deleteProduct":
                    handleDeleteProduct(request, response);
                    break;
                case "updateOrderStatus":
                    handleUpdateOrderStatus(request, response);
                    break;
                default:
                    response.sendRedirect("admin?tab=" + tab + "&error=Invalid action");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin?tab=" + tab + "&error=Operation failed");
        }
    }

    // Existing user methods...
    private void handleAddUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int roleId = Integer.parseInt(request.getParameter("roleId"));

        UserInfo newUser = userServices.register(email, password, username, roleId);

        if (newUser != null) {
            response.sendRedirect("admin?tab=users&success=User added successfully");
        } else {
            response.sendRedirect("admin?tab=users&error=Failed to add user");
        }
    }

    private void handleEditUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        int newRoleId = Integer.parseInt(request.getParameter("newRoleId"));

        boolean success = UserDbImp.updateUserRole(userId, newRoleId);

        if (success) {
            response.sendRedirect("admin?tab=users&success=User role updated successfully");
        } else {
            response.sendRedirect("admin?tab=users&error=Failed to update user role");
        }
    }

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int userId = Integer.parseInt(request.getParameter("userId"));

        UserInfo currentUser = (UserInfo) request.getSession().getAttribute("userInfo");
        if (userId == currentUser.getId()) {
            response.sendRedirect("admin?tab=users&error=Cannot delete your own account");
            return;
        }

        boolean success = UserDbImp.deleteUser(userId);

        if (success) {
            response.sendRedirect("admin?tab=users&success=User deleted successfully");
        } else {
            response.sendRedirect("admin?tab=users&error=Failed to delete user");
        }
    }

    private void handleAddProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));  // Change to double
        int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
        int categoryid = Integer.parseInt(request.getParameter("categoryid"));

        boolean success = ProductDbImp.addProduct(name, description, price, stockQuantity, categoryid);

        if (success) {
            response.sendRedirect("admin?tab=products&success=Product added successfully");
        } else {
            response.sendRedirect("admin?tab=products&error=Failed to add product");
        }
    }

    private void handleEditProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));  // Change to double
        int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
        int categoryid = Integer.parseInt(request.getParameter("categoryid"));

        boolean success = ProductDbImp.updateProduct(productId, name, description, price, stockQuantity, categoryid);

        if (success) {
            response.sendRedirect("admin?tab=products&success=Product updated successfully");
        } else {
            response.sendRedirect("admin?tab=products&error=Failed to update product");
        }
    }


    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int productId = Integer.parseInt(request.getParameter("productId"));

        boolean success = ProductDbImp.deleteProduct(productId);

        if (success) {
            response.sendRedirect("admin?tab=products&success=Product deleted successfully");
        } else {
            response.sendRedirect("admin?tab=products&error=Failed to delete product");
        }
    }

    // Order methods
    private void handleUpdateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String newStatus = request.getParameter("newStatus");

        boolean success = OrderDbImp.updateOrderStatus(orderId, newStatus);

        if (success) {
            response.sendRedirect("admin?tab=orders&success=Order status updated successfully");
        } else {
            response.sendRedirect("admin?tab=orders&error=Failed to update order status");
        }
    }
}