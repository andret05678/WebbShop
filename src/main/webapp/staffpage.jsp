<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.UI.Info.UserInfo" %>
<%@ page import="com.UI.Info.ProductInfo" %>
<%@ page import="com.UI.Info.OrderInfo" %>
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
    List<ProductInfo> allProducts = (List<ProductInfo>) request.getAttribute("allProducts");
    List<OrderInfo> allOrders = (List<OrderInfo>) request.getAttribute("allOrders");
    String currentTab = (String) request.getAttribute("currentTab");
    if (currentTab == null) currentTab = "users";

    String success = request.getParameter("success");
    String error = request.getParameter("error");
%>
<html>
<head>
    <title>Staff Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 1400px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .tabs { margin-bottom: 20px; }
        .tab { padding: 10px 20px; background: #e9ecef; border: none; cursor: pointer; margin-right: 5px; }
        .tab.active { background: #007bff; color: white; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #f8f9fa; font-weight: bold; }
        .btn { padding: 6px 12px; border: none; border-radius: 4px; cursor: pointer; margin: 2px; }
        .btn-edit { background: #ffc107; color: black; }
        .btn-delete { background: #dc3545; color: white; }
        .btn-save { background: #28a745; color: white; }
        .btn-update { background: #17a2b8; color: white; }
        .role-customer { color: #17a2b8; }
        .role-staff { color: #fd7e14; }
        .role-admin { color: #dc3545; font-weight: bold; }
        .status-pending { color: #ffc107; font-weight: bold; }
        .status-processing { color: #17a2b8; font-weight: bold; }
        .status-completed { color: #28a745; font-weight: bold; }
        .status-cancelled { color: #dc3545; font-weight: bold; }
        .success { color: #28a745; background: #d4edda; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        .error { color: #dc3545; background: #f8d7da; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        .form-row { display: flex; gap: 10px; margin-bottom: 10px; align-items: end; }
        .form-group { flex: 1; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input, .form-group select, .form-group textarea { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
        .order-items { background: #f8f9fa; padding: 10px; border-radius: 4px; margin-top: 5px; }
        .order-item { display: flex; justify-content: space-between; padding: 5px 0; border-bottom: 1px solid #dee2e6; }
        .order-item:last-child { border-bottom: none; }
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

    <% if (success != null) { %>
    <div class="success"><%= success %></div>
    <% } %>
    <% if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <div class="tabs">
        <button class="tab <%= "users".equals(currentTab) ? "active" : "" %>" onclick="showTab('users')">User Management</button>
        <button class="tab <%= "products".equals(currentTab) ? "active" : "" %>" onclick="showTab('products')">Product Management</button>
        <button class="tab <%= "orders".equals(currentTab) ? "active" : "" %>" onclick="showTab('orders')">Order Management</button>
    </div>

    <!-- Order Management Tab -->
    <div id="orders-tab" class="tab-content" style="display: <%= "orders".equals(currentTab) ? "block" : "none" %>;">
        <h2>Order Management</h2>

        <!-- Orders List -->
        <table>
            <thead>
            <tr>
                <th>Order ID</th>
                <th>User Email</th>
                <th>Status</th>
                <th>Total Amount</th>
                <th>Order Date</th>
                <th>Items</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <% if (allOrders != null && !allOrders.isEmpty()) {
                for (OrderInfo order : allOrders) {
                    String statusClass = "";
                    switch(order.getStatus().toLowerCase()) {
                        case "pending": statusClass = "status-pending"; break;
                        case "processing": statusClass = "status-processing"; break;
                        case "completed": statusClass = "status-completed"; break;
                        case "cancelled": statusClass = "status-cancelled"; break;
                        default: statusClass = "status-pending";
                    }
            %>
            <tr>
                <td><%= order.getId() %></td>
                <td><%= order.getUserEmail() %></td>
                <td><span class="<%= statusClass %>"><%= order.getStatus() %></span></td>
                <td>$<%= order.getTotalAmount() %></td>
                <td><%= order.getOrderDate() %></td>
                <td>
                    <div class="order-items">
                        <% if (order.getItems() != null && !order.getItems().isEmpty()) {
                            for (var item : order.getItems()) { %>
                        <div class="order-item">
                            <span><%= item.getProductName() %></span>
                            <span>Qty: <%= item.getQuantity() %></span>
                            <span>$<%= item.getPrice() %></span>
                        </div>
                        <% }
                        } else { %>
                        No items
                        <% } %>
                    </div>
                </td>
                <td>
                    <form action="admin" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="updateOrderStatus">
                        <input type="hidden" name="tab" value="orders">
                        <input type="hidden" name="orderId" value="<%= order.getId() %>">
                        <select name="newStatus" style="padding: 4px;">
                            <option value="pending" <%= "pending".equalsIgnoreCase(order.getStatus()) ? "selected" : "" %>>Pending</option>
                            <option value="processing" <%= "processing".equalsIgnoreCase(order.getStatus()) ? "selected" : "" %>>Processing</option>
                            <option value="completed" <%= "completed".equalsIgnoreCase(order.getStatus()) ? "selected" : "" %>>Completed</option>
                            <option value="cancelled" <%= "cancelled".equalsIgnoreCase(order.getStatus()) ? "selected" : "" %>>Cancelled</option>
                        </select>
                        <button type="submit" class="btn btn-update">Update Status</button>
                    </form>
                </td>
            </tr>
            <% }
            } else { %>
            <tr>
                <td colspan="7" style="text-align: center;">No orders found</td>
            </tr>
            <% } %>
            </tbody>
        </table>
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

        // Update URL without reloading page
        const url = new URL(window.location);
        url.searchParams.set('tab', tabName);
        window.history.replaceState({}, '', url);
    }

    // Set active tab on page load based on URL parameter
    document.addEventListener('DOMContentLoaded', function() {
        const urlParams = new URLSearchParams(window.location.search);
        const tab = urlParams.get('tab');
        if (tab) {
            showTabByName(tab);
        }
    });

    function showTabByName(tabName) {
        document.querySelectorAll('.tab-content').forEach(tab => {
            tab.style.display = 'none';
        });
        document.querySelectorAll('.tab').forEach(tab => {
            tab.classList.remove('active');
        });
        document.getElementById(tabName + '-tab').style.display = 'block';
        document.querySelector(`.tab[onclick="showTab('${tabName}')"]`).classList.add('active');
    }
</script>
</body>
</html>