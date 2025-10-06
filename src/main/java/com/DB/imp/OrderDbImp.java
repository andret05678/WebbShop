package com.DB.imp;
import com.BO.Order;
import com.DB.supa;
import com.UI.Info.OrderInfo;

import java.sql.*;



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

            // Get order items
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
        Statement stmt = conn.createStatement();
        String sql = "SELECT * WHERE ID=" + id;
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return Order.createOrder(id,rs.getInt("UserID"),rs.getString("Status"));
        }
        return null;
    }
    public static boolean insert(Order order) throws SQLException {
        Connection conn = supa.getConnection();

        String sql = "INSERT INTO order (id, userId, status) VALUES (?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, order.getId());
        pstmt.setInt(2, order.getUserId());
        pstmt.setString(3, order.getStatus());
        int rowsAffected = pstmt.executeUpdate();

        pstmt.close();
        return rowsAffected > 0;

    }

    private static List<OrderItemInfo> getOrderItems(int orderId) throws SQLException {
        List<OrderItemInfo> items = new ArrayList<>();
        Connection conn = supa.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
                "SELECT oi.id, oi.order_id, oi.product_id, p.name, oi.quantity, oi.price " +
                        "FROM order_item oi " +
                        "JOIN product p ON oi.product_id = p.id " +
                        "WHERE oi.order_id = ?"
        );

        pstmt.setInt(1, orderId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            OrderItemInfo item = new OrderItemInfo(
                    rs.getInt("id"),
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
}