package com.UI;

import com.BO.Services.UserServices;
import com.BO.User;
import com.UI.Info.UserInfo;
import com.DB.imp.UserDbImp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
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

        try {
            // Get all users for display
            List<User> allUsers = UserDbImp.getAllUsers();
            request.setAttribute("allUsers", allUsers);

            request.getRequestDispatcher("/adminpage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin?error=Unable to load users");
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
                default:
                    response.sendRedirect("admin?error=Invalid action");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin?error=Operation failed");
        }
    }

    private void handleAddUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int roleId = Integer.parseInt(request.getParameter("roleId"));

        // Use your existing registration service
        UserInfo newUser = userServices.register(email, password, username, roleId);

        if (newUser != null) {
            response.sendRedirect("admin?success=User added successfully");
        } else {
            response.sendRedirect("admin?error=Failed to add user - email may already exist");
        }
    }

    private void handleEditUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        int newRoleId = Integer.parseInt(request.getParameter("newRoleId"));

        boolean success = UserDbImp.updateUserRole(userId, newRoleId);

        if (success) {
            response.sendRedirect("admin?success=User role updated successfully");
        } else {
            response.sendRedirect("admin?error=Failed to update user role");
        }
    }

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int userId = Integer.parseInt(request.getParameter("userId"));

        // Prevent admin from deleting themselves
        UserInfo currentUser = (UserInfo) request.getSession().getAttribute("userInfo");
        if (userId == currentUser.getId()) {
            response.sendRedirect("admin?error=Cannot delete your own account");
            return;
        }

        boolean success = UserDbImp.deleteUser(userId);

        if (success) {
            response.sendRedirect("admin?success=User deleted successfully");
        } else {
            response.sendRedirect("admin?error=Failed to delete user");
        }
    }
}