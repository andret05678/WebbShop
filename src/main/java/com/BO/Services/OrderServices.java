package com.BO.Services;

import com.DB.imp.OrderDbImp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderServices {

    public OrderServices() {
    }

    public String placeOrder(int userId, List<Map<String, String>> cartItems) throws SQLException {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://aws-1-eu-north-1.pooler.supabase.com:5432/postgres?user=postgres.yibhllavyovhbjaxwynu&password=Anton056780990";
            conn = DriverManager.getConnection(url);

            conn.setAutoCommit(false);

            if (cartItems == null || cartItems.isEmpty()) {
                throw new SQLException("Cart is empty");
            }

            double totalAmount = 0.0;
            for (Map<String, String> item : cartItems) {
                totalAmount += Double.parseDouble(item.get("price"));
            }

            for (Map<String, String> item : cartItems) {
                int productId = Integer.parseInt(item.get("id"));
                if (!isProductInStock(conn, productId)) {
                    throw new SQLException("Product " + item.get("name") + " is out of stock");
                }
            }

            int orderId = OrderDbImp.insertOrder(conn, userId, totalAmount);

            for (Map<String, String> item : cartItems) {
                int productId = Integer.parseInt(item.get("id"));
                double price = Double.parseDouble(item.get("price"));

                OrderDbImp.insertOrderItem(conn, orderId, productId, price);

                updateProductStock(conn, productId);
            }

            conn.commit();
            return "Order placed successfully! Order ID: " + orderId;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            throw new SQLException("Failed to place order: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    private boolean isProductInStock(Connection conn, int productId) throws SQLException {
        String sql = "SELECT stock FROM product WHERE id = ? AND stock > 0";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            var rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    private void updateProductStock(Connection conn, int productId) throws SQLException {
        String sql = "UPDATE product SET stock = stock - 1 WHERE id = ? AND stock > 0";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Failed to update stock for product ID: " + productId);
            }
        }
    }
}