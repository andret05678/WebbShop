package com.BO.Services;

import com.DB.imp.OrderDbImp;
import com.DB.supa;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderServices {

    public OrderServices() {
    }

    public String placeOrder(int userId, List<Map<String, String>> cartItems) throws SQLException {
        Connection conn = null;
        try {
            conn = supa.getConnection();
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
                if (!ProductServices.isProductInStock(productId)) {
                    throw new SQLException("Product " + item.get("name") + " is out of stock");
                }
            }

            int orderId = OrderDbImp.insertOrder(conn, userId, totalAmount);

            for (Map<String, String> item : cartItems) {
                int productId = Integer.parseInt(item.get("id"));
                double price = Double.parseDouble(item.get("price"));

                OrderDbImp.insertOrderItem(conn, orderId, productId, price);

                ProductServices.updateProductStock(productId);
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

}