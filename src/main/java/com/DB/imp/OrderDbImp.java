package com.DB.imp;

import com.BO.Order;
import com.DB.supa;
import com.UI.Info.OrderInfo;
import com.UI.Info.OrderItemInfo;
import com.UI.Info.ProductInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDbImp {

    public static List<OrderInfo> getAllOrders() throws SQLException {
        List<OrderInfo> orders = new ArrayList<>();
        Connection conn = supa.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT o.id, o.user_id, u.email, o.status, o.total_amount, o.order_date " +
                        "FROM orders o " +
                        "JOIN users u ON o.user_id = u.id " +
                        "ORDER BY o.order_date DESC"
        );

        while (rs.next()) {
            OrderInfo order = new OrderInfo(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("email"),
                    rs.getString("status"),
                    rs.getBigDecimal("total_amount"),
                    rs.getTimestamp("order_date")
            );

            List<OrderItemInfo> items = getOrderItems(order.getId());
            order.setItems(items);

            orders.add(order);
        }

        rs.close();
        stmt.close();
        conn.close();

        return orders;
    }

    public static Order findById(int id) throws SQLException {
        Connection conn = supa.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orders WHERE id = ?");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return Order.createOrder(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("status")
            );
        }
        return null;
    }

    public static boolean insert(Order order) throws SQLException {
        Connection conn = supa.getConnection();
        String sql = "INSERT INTO orders (user_id, status, total_amount) VALUES (?, ?, 0)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, order.getUserId());
        pstmt.setString(2, order.getStatus());
        int rowsAffected = pstmt.executeUpdate();

        pstmt.close();
        conn.close();
        return rowsAffected > 0;
    }

    private static List<OrderItemInfo> getOrderItems(int orderId) throws SQLException {
        List<OrderItemInfo> items = new ArrayList<>();
        Connection conn = supa.getConnection();

        // Updated query without the oi.id column
        String sql = "SELECT oi.order_id, oi.product_id, p.name, oi.quantity, oi.price " +
                "FROM order_item oi " +
                "JOIN product p ON oi.product_id = p.id " +
                "WHERE oi.order_id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, orderId);
        ResultSet rs = pstmt.executeQuery();

        int itemCounter = 1; // Use counter as temporary ID since there's no id column
        while (rs.next()) {
            OrderItemInfo item = new OrderItemInfo(
                    itemCounter++, // Use counter as temporary ID
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
            );
            items.add(item);
        }

        rs.close();
        pstmt.close();
        conn.close();

        return items;
    }

    public static boolean updateOrderStatus(int orderId, String newStatus) throws SQLException {
        Connection conn = supa.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("UPDATE orders SET status = ? WHERE id = ?");
        pstmt.setString(1, newStatus);
        pstmt.setInt(2, orderId);
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();
        conn.close();
        return rowsAffected > 0;
    }

    public static boolean deleteOrder(int orderId) throws SQLException {
        Connection conn = supa.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM orders WHERE id = ?");
        pstmt.setInt(1, orderId);
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();
        conn.close();
        return rowsAffected > 0;
    }

    public static boolean updateProductStock(int productId, int newStock) throws SQLException {
        Connection conn = supa.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("UPDATE product SET stock = ? WHERE id = ?");
        pstmt.setInt(1, newStock);
        pstmt.setInt(2, productId);
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();
        conn.close();
        return rowsAffected > 0;
    }

    public static int insertOrder(Connection conn, int userId, double totalAmount) throws SQLException {
        String sql = "INSERT INTO orders (user_id, total_amount, status, order_date) VALUES (?, ?, 'pending', NOW()) RETURNING id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setBigDecimal(2, java.math.BigDecimal.valueOf(totalAmount));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Failed to get order ID");
        }
    }

    public static void insertOrderItem(Connection conn, int orderId, int productId, double price) throws SQLException {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, price) VALUES (?, ?, 1, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);
            pstmt.setBigDecimal(3, java.math.BigDecimal.valueOf(price));
            pstmt.executeUpdate();
        }
    }

    public static boolean updateProductBasicInfo(int productId, String name, String description, double price, int stock) throws SQLException {
        Connection conn = supa.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE product SET name = ?, description = ?, price = ?, stock = ? WHERE id = ?"
        );

        pstmt.setString(1, name);
        pstmt.setString(2, description);
        pstmt.setDouble(3, price);
        pstmt.setInt(4, stock);
        pstmt.setInt(5, productId);

        int rowsAffected = pstmt.executeUpdate();

        pstmt.close();
        conn.close();

        return rowsAffected > 0;
    }

    public static List<ProductInfo> getLowStockProducts() throws SQLException {
        List<ProductInfo> products = new ArrayList<>();
        Connection conn = supa.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
                "SELECT id, name, description, price, stock, categoryid FROM product WHERE stock < 10 ORDER BY stock ASC"
        );

        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            ProductInfo product = new ProductInfo(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getInt("categoryid")
            );
            products.add(product);
        }

        rs.close();
        pstmt.close();
        conn.close();

        return products;
    }

    // Add this method to check if product is in stock
    public static boolean isProductInStock(Connection conn, int productId) throws SQLException {
        String sql = "SELECT stock FROM product WHERE id = ? AND stock > 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }
}