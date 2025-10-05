<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.UI.Info.UserInfo" %>
<%
    // Check if user is logged in
    UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
    if (userInfo == null) {
        response.sendRedirect("login");
        return;
    }
%>
<html>
<head>
    <title>Store</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f5f5f5; margin: 0; padding: 20px; }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; padding: 10px; background: white; border-radius: 8px; }
        h2 { text-align: center; color: #333; }
        table { width: 80%; margin: 20px auto; border-collapse: collapse; background: white; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        th, td { padding: 12px; text-align: center; border: 1px solid #ddd; }
        th { background: #007BFF; color: white; }
        tr:nth-child(even) { background: #f9f9f9; }
        form { margin: 0; }
        .btn { background: #28a745; color: white; padding: 8px 16px; border: none; cursor: pointer; border-radius: 4px; text-decoration: none; display: inline-block; }
        .btn:hover { background: #218838; }
        .btn-cart { background: #6c757d; }
        .btn-cart:hover { background: #545b62; }
        .btn-logout { background: #dc3545; }
        .btn-logout:hover { background: #c82333; }
        .user-info { background: #e9ecef; padding: 10px; border-radius: 4px; margin-bottom: 10px; }
    </style>
</head>
<body>
<div class="header">
    <h1>Welcome to the Store</h1>
    <div>
            <span class="user-info">
                Welcome, <%= userInfo.getUsername() %>
                <%
                    String role = "";
                    switch(userInfo.getRole()) {
                        case 1: role = "Customer"; break;
                        case 2: role = "Staff"; break;
                        case 3: role = "Admin"; break;
                    }
                %>
                (<%= role %>)
            </span>
        <a href="cart" class="btn btn-cart">View Cart</a>
        <% if (userInfo.getRole() == 3) { %>
        <a href="admin" class="btn" style="background: #fd7e14;">Admin Panel</a>
        <% } %>
        <a href="logout" class="btn btn-logout">Logout</a>
    </div>
</div>

<%
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    try {
        // Load PostgreSQL driver
        Class.forName("org.postgresql.Driver");

        // Database connection
        String url = "jdbc:postgresql://aws-1-eu-north-1.pooler.supabase.com:5432/postgres?user=postgres.yibhllavyovhbjaxwynu&password=Anton056780990";
        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT id, name, price FROM Product LIMIT 5");
%>

<h2>Available Products</h2>

<table>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>Action</th>
    </tr>
    <%
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String price = rs.getBigDecimal("price").toString();
    %>
    <tr>
        <td><%= id %></td>
        <td><%= name %></td>
        <td>$<%= price %></td>
        <td>
            <form method='post' action='cart'>
                <input type='hidden' name='id' value='<%= id %>'/>
                <input type='hidden' name='name' value='<%= name %>'/>
                <input type='hidden' name='price' value='<%= price %>'/>
                <input type='hidden' name='action' value='add'/>
                <input type='submit' class='btn' value='Add to Cart'/>
            </form>
        </td>
    </tr>
    <%
        }
    %>
</table>

<%
} catch (ClassNotFoundException e) {
%>
<div style="text-align: center; color: red;">
    <h2>PostgreSQL Driver not found</h2>
    <pre><%= e.getMessage() %></pre>
</div>
<%
} catch (SQLException e) {
%>
<div style="text-align: center; color: red;">
    <h2>Database Connection Failed</h2>
    <pre><%= e.getMessage() %></pre>
</div>
<%
    } finally {
        // Close resources
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
%>

</body>
</html>