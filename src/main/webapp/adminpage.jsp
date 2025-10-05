<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.UI.Info.UserInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.BO.User" %>
<%
    // Check if user is logged in and is admin
    UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
    if (userInfo == null || userInfo.getRole() != 3) {
        response.sendRedirect("login");
        return;
    }

    List<User> allUsers = (List<User>) request.getAttribute("allUsers");
%>
<html>
<head>
    <title>Admin Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1200px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .tabs { margin-bottom: 20px; }
        .tab { padding: 10px 20px; background: #e9ecef; border: none; cursor: pointer; }
        .tab.active { background: #007bff; color: white; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #f8f9fa; }
        .btn { padding: 6px 12px; border: none; border-radius: 4px; cursor: pointer; }
        .btn-edit { background: #ffc107; color: black; }
        .btn-delete { background: #dc3545; color: white; }
        .btn-save { background: #28a745; color: white; }
        .role-customer { color: #17a2b8; }
        .role-staff { color: #fd7e14; }
        .role-admin { color: #dc3545; font-weight: bold; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Admin Dashboard</h1>
        <div>
            <span>Welcome, <%= userInfo.getUsername() %> (Admin)</span> |
            <a href="testpage">Store Front</a> |
            <a href="logout">Logout</a>
        </div>
    </div>

    <div class="tabs">
        <button class="tab active" onclick="showTab('users')">User Management</button>
        <button class="tab" onclick="showTab('products')">Product Management</button>
        <button class="tab" onclick="showTab('orders')">Order Management</button>
    </div>

    <!-- User Management Tab -->
    <div id="users-tab" class="tab-content">
        <h2>User Management</h2>

        <!-- Add New User Form -->
        <div style="background: #f8f9fa; padding: 15px; margin-bottom: 20px; border-radius: 5px;">
            <h3>Add New User</h3>
            <form action="admin" method="post">
                <input type="hidden" name="action" value="addUser">
                <table style="width: auto;">
                    <tr>
                        <td><input type="email" name="email" placeholder="Email" required style="padding: 8px; width: 200px;"></td>
                        <td><input type="text" name="username" placeholder="Username" required style="padding: 8px; width: 150px;"></td>
                        <td><input type="password" name="password" placeholder="Password" required style="padding: 8px; width: 150px;"></td>
                        <td>
                            <select name="roleId" style="padding: 8px;">
                                <option value="1">Customer</option>
                                <option value="2">Staff</option>
                                <option value="3">Admin</option>
                            </select>
                        </td>
                        <td><button type="submit" class="btn btn-save">Add User</button></td>
                    </tr>
                </table>
            </form>
        </div>

        <!-- Users List -->
        <h3>All Users</h3>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Email</th>
                <th>Username</th>
                <th>Role</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <% if (allUsers != null && !allUsers.isEmpty()) {
                for (User user : allUsers) {
                    String roleClass = "";
                    String roleName = "";
                    switch(user.getRoleId()) {
                        case 1:
                            roleClass = "role-customer";
                            roleName = "Customer";
                            break;
                        case 2:
                            roleClass = "role-staff";
                            roleName = "Staff";
                            break;
                        case 3:
                            roleClass = "role-admin";
                            roleName = "Admin";
                            break;
                    }
            %>
            <tr>
                <td><%= user.getId() %></td>
                <td><%= user.getEmail() %></td>
                <td><%= user.getUsername() %></td>
                <td><span class="<%= roleClass %>"><%= roleName %></span></td>
                <td>
                    <form action="admin" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="editUser">
                        <input type="hidden" name="userId" value="<%= user.getId() %>">
                        <select name="newRoleId" style="padding: 4px;">
                            <option value="1" <%= user.getRoleId() == 1 ? "selected" : "" %>>Customer</option>
                            <option value="2" <%= user.getRoleId() == 2 ? "selected" : "" %>>Staff</option>
                            <option value="3" <%= user.getRoleId() == 3 ? "selected" : "" %>>Admin</option>
                        </select>
                        <button type="submit" class="btn btn-edit">Update Role</button>
                    </form>
                    <% if (user.getId() != userInfo.getId()) { %>
                    <form action="admin" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="deleteUser">
                        <input type="hidden" name="userId" value="<%= user.getId() %>">
                        <button type="submit" class="btn btn-delete" onclick="return confirm('Are you sure you want to delete this user?')">Delete</button>
                    </form>
                    <% } %>
                </td>
            </tr>
            <% }
            } else { %>
            <tr>
                <td colspan="5" style="text-align: center;">No users found</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <!-- Other tabs can be added later -->
    <div id="products-tab" class="tab-content" style="display: none;">
        <h2>Product Management</h2>
        <p>Product management functionality coming soon...</p>
    </div>

    <div id="orders-tab" class="tab-content" style="display: none;">
        <h2>Order Management</h2>
        <p>Order management functionality coming soon...</p>
    </div>
</div>

<script>
    function showTab(tabName) {
        // Hide all tab contents
        document.querySelectorAll('.tab-content').forEach(tab => {
            tab.style.display = 'none';
        });

        // Remove active class from all tabs
        document.querySelectorAll('.tab').forEach(tab => {
            tab.classList.remove('active');
        });

        // Show selected tab and set active
        document.getElementById(tabName + '-tab').style.display = 'block';
        event.target.classList.add('active');
    }
</script>
</body>
</html>