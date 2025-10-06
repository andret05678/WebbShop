<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.UI.Info.UserInfo" %>
<%@ page import="com.UI.Info.ProductInfo" %>
<%@ page import="com.UI.Info.OrderInfo" %>
<%@ page import="java.util.List" %>
<%
    // Check if user is logged in and is staff (role 2) or admin (role 3)
    UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
    if (userInfo == null || (userInfo.getRole() != 2 && userInfo.getRole() != 3)) {
        response.sendRedirect("login");
        return;
    }

    List<OrderInfo> allOrders = (List<OrderInfo>) request.getAttribute("allOrders");
    List<ProductInfo> allProducts = (List<ProductInfo>) request.getAttribute("allProducts");
    List<ProductInfo> lowStockProducts = (List<ProductInfo>) request.getAttribute("lowStockProducts");
    String currentTab = (String) request.getAttribute("currentTab");
    if (currentTab == null) currentTab = "orders";

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
        .tab.active { background: #17a2b8; color: white; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #f8f9fa; font-weight: bold; }
        .btn { padding: 6px 12px; border: none; border-radius: 4px; cursor: pointer; margin: 2px; }
        .btn-update { background: #17a2b8; color: white; }
        .btn-save { background: #28a745; color: white; }
        .btn-warning { background: #ffc107; color: black; }
        .status-pending { color: #ffc107; font-weight: bold; }
        .status-processing { color: #17a2b8; font-weight: bold; }
        .status-completed { color: #28a745; font-weight: bold; }
        .status-cancelled { color: #dc3545; font-weight: bold; }
        .stock-low { color: #dc3545; font-weight: bold; background: #f8d7da; padding: 4px 8px; border-radius: 4px; }
        .stock-medium { color: #ffc107; font-weight: bold; }
        .stock-good { color: #28a745; font-weight: bold; }
        .success { color: #28a745; background: #d4edda; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        .error { color: #dc3545; background: #f8d7da; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        .alert-warning { color: #856404; background: #fff3cd; padding: 10px; border-radius: 4px; margin-bottom: 15px; border: 1px solid #ffeaa7; }
        .order-items { background: #f8f9fa; padding: 10px; border-radius: 4px; margin-top: 5px; }
        .order-item { display: flex; justify-content: space-between; padding: 5px 0; border-bottom: 1px solid #dee2e6; }
        .order-item:last-child { border-bottom: none; }
        .form-inline { display: inline; }
        .form-inline input, .form-inline select { padding: 4px; margin: 0 5px; border: 1px solid #ddd; border-radius: 4px; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Staff Dashboard</h1>
        <div>
            <span>Welcome, <%= userInfo.getUsername() %> (<%= userInfo.getRole() == 2 ? "Staff" : "Admin" %>)</span> |
            <a href="testpage">Store Front</a> |
            <% if (userInfo.getRole() == 3) { %>
            <a href="admin">Admin Panel</a> |
            <% } %>
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
        <button class="tab <%= "orders".equals(currentTab) ? "active" : "" %>" onclick="showTab('orders')">Order Management</button>
        <button class="tab <%= "products".equals(currentTab) ? "active" : "" %>" onclick="showTab('products')">Product Inventory</button>
        <button class="tab <%= "inventory".equals(currentTab) ? "active" : "" %>" onclick="showTab('inventory')">Low Stock Alerts</button>
    </div>

    <!-- Order Management Tab -->
    <div id="orders-tab" class="tab-content" style="display: <%= "orders".equals(currentTab) ? "block" : "none" %>;">
        <h2>Order Management</h2>

        <% if (allOrders != null && !allOrders.isEmpty()) { %>
        <table>
            <thead>
            <tr>
                <th>Order ID</th>
                <th>Customer Email</th>
                <th>Status</th>
                <th>Total Amount</th>
                <th>Order Date</th>
                <th>Items</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <% for (OrderInfo order : allOrders) {
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
                <td>$<%= String.format("%.2f", order.getTotalAmount()) %></td>
                <td><%= order.getOrderDate() %></td>
                <td>
                    <div class="order-items">
                        <% if (order.getItems() != null && !order.getItems().isEmpty()) {
                            for (var item : order.getItems()) { %>
                        <div class="order-item">
                            <span><%= item.getProductName() %></span>
                            <span>Qty: <%= item.getQuantity() %></span>
                            <span>$<%= String.format("%.2f", item.getPrice()) %></span>
                        </div>
                        <% }
                        } else { %>
                        No items
                        <% } %>
                    </div>
                </td>
                <td>
                    <form class="form-inline" action="staffpage" method="post">
                        <input type="hidden" name="action" value="updateOrderStatus">
                        <input type="hidden" name="tab" value="orders">
                        <input type="hidden" name="orderId" value="<%= order.getId() %>">
                        <select name="newStatus">
                            <option value="pending" <%= "pending".equalsIgnoreCase(order.getStatus()) ? "selected" : "" %>>Pending</option>
                            <option value="processing" <%= "processing".equalsIgnoreCase(order.getStatus()) ? "selected" : "" %>>Processing</option>
                            <option value="completed" <%= "completed".equalsIgnoreCase(order.getStatus()) ? "selected" : "" %>>Completed</option>
                            <option value="cancelled" <%= "cancelled".equalsIgnoreCase(order.getStatus()) ? "selected" : "" %>>Cancelled</option>
                        </select>
                        <button type="submit" class="btn btn-update">Update</button>
                    </form>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <% } else { %>
        <p>No orders found.</p>
        <% } %>
    </div>

    <!-- Product Inventory Tab -->
    <div id="products-tab" class="tab-content" style="display: <%= "products".equals(currentTab) ? "block" : "none" %>;">
        <h2>Product Inventory Management</h2>

        <% if (allProducts != null && !allProducts.isEmpty()) { %>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
                <th>Price</th>
                <th>Stock</th>
                <th>Category ID</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <% for (ProductInfo product : allProducts) {
                String stockClass = "";
                if (product.getStock() < 5) {
                    stockClass = "stock-low";
                } else if (product.getStock() < 10) {
                    stockClass = "stock-medium";
                } else {
                    stockClass = "stock-good";
                }
            %>
            <tr>
                <td><%= product.getId() %></td>
                <td><%= product.getName() %></td>
                <td><%= product.getDescription() %></td>
                <td>$<%= String.format("%.2f", product.getPrice()) %></td>
                <td><span class="<%= stockClass %>"><%= product.getStock() %></span></td>
                <td><%= product.getCategoryId() %></td>
                <td>
                    <!-- Quick Stock Update -->
                    <form class="form-inline" action="staffpage" method="post" style="margin-bottom: 5px;">
                        <input type="hidden" name="action" value="updateProductStock">
                        <input type="hidden" name="tab" value="products">
                        <input type="hidden" name="productId" value="<%= product.getId() %>">
                        <input type="number" name="newStock" value="<%= product.getStock() %>" min="0" style="width: 80px;">
                        <button type="submit" class="btn btn-update">Update Stock</button>
                    </form>

                    <!-- Full Product Edit -->
                    <form class="form-inline" action="staffpage" method="post">
                        <input type="hidden" name="action" value="updateProduct">
                        <input type="hidden" name="tab" value="products">
                        <input type="hidden" name="productId" value="<%= product.getId() %>">
                        <input type="text" name="name" value="<%= product.getName() %>" style="width: 120px;" placeholder="Name" required>
                        <input type="text" name="description" value="<%= product.getDescription() %>" style="width: 150px;" placeholder="Description" required>
                        <input type="number" name="price" step="0.01" value="<%= product.getPrice() %>" style="width: 80px;" placeholder="Price" required>
                        <input type="number" name="stock" value="<%= product.getStock() %>" style="width: 60px;" placeholder="Stock" required>
                        <button type="submit" class="btn btn-save">Save</button>
                    </form>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <% } else { %>
        <p>No products found.</p>
        <% } %>
    </div>

    <!-- Low Stock Alerts Tab -->
    <div id="inventory-tab" class="tab-content" style="display: <%= "inventory".equals(currentTab) ? "block" : "none" %>;">
        <h2>Low Stock Alerts</h2>

        <% if (lowStockProducts != null && !lowStockProducts.isEmpty()) { %>
        <div class="alert-warning">
            <strong>Warning:</strong> <%= lowStockProducts.size() %> product(s) are running low on stock!
        </div>

        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
                <th>Price</th>
                <th>Current Stock</th>
                <th>Category ID</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <% for (ProductInfo product : lowStockProducts) { %>
            <tr>
                <td><%= product.getId() %></td>
                <td><strong><%= product.getName() %></strong></td>
                <td><%= product.getDescription() %></td>
                <td>$<%= String.format("%.2f", product.getPrice()) %></td>
                <td><span class="stock-low"><%= product.getStock() %> (LOW)</span></td>
                <td><%= product.getCategoryId() %></td>
                <td>
                    <form class="form-inline" action="staffpage" method="post">
                        <input type="hidden" name="action" value="updateProductStock">
                        <input type="hidden" name="tab" value="inventory">
                        <input type="hidden" name="productId" value="<%= product.getId() %>">
                        <input type="number" name="newStock" value="<%= product.getStock() + 20 %>" min="<%= product.getStock() + 1 %>" style="width: 80px;">
                        <button type="submit" class="btn btn-warning">Restock</button>
                    </form>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <% } else { %>
        <div class="success">
            <strong>Great news!</strong> All products have sufficient stock levels.
        </div>
        <% } %>
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