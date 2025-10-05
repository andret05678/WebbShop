package com.DB.imp;

import com.UI.Info.ProductInfo;
import com.DB.supa;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDbImp {

    public static List<ProductInfo> getAllProducts() throws SQLException {
        List<ProductInfo> products = new ArrayList<>();
        Connection conn = supa.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, name, description, price, stock, categoryid FROM product ORDER BY id");

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
        stmt.close();
        conn.close();

        return products;
    }

    public static boolean addProduct(String name, String description, double price, int stock, int categoryid) throws SQLException {
        Connection conn = supa.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO product (name, description, price, stock, categoryid) VALUES (?, ?, ?, ?, ?)"
        );

        pstmt.setString(1, name);
        pstmt.setString(2, description);
        pstmt.setDouble(3, price);
        pstmt.setInt(4, stock);
        pstmt.setInt(5, categoryid);

        int rowsAffected = pstmt.executeUpdate();

        pstmt.close();
        conn.close();

        return rowsAffected > 0;
    }

    public static boolean updateProduct(int productId, String name, String description, double price, int stockQuantity, int categoryid) throws SQLException {
        Connection conn = supa.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE product SET name = ?, description = ?, price = ?, stock = ?, categoryid = ? WHERE id = ?"
        );

        pstmt.setString(1, name);
        pstmt.setString(2, description);
        pstmt.setDouble(3, price);
        pstmt.setInt(4, stockQuantity);
        pstmt.setInt(5, categoryid);
        pstmt.setInt(6, productId);

        int rowsAffected = pstmt.executeUpdate();

        pstmt.close();
        conn.close();

        return rowsAffected > 0;
    }

    public static boolean deleteProduct(int productId) throws SQLException {
        Connection conn = supa.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM product WHERE id = ?");

        pstmt.setInt(1, productId);

        int rowsAffected = pstmt.executeUpdate();

        pstmt.close();
        conn.close();

        return rowsAffected > 0;
    }

    public static ProductInfo findById(int productId) throws SQLException {
        Connection conn = supa.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(
                    "SELECT id, name, description, price, stock, categoryid FROM product WHERE id = ?"
            );
            pstmt.setInt(1, productId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new ProductInfo(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getInt("categoryid")
                );
            }
            return null; // Product not found

        } finally {
            // Close resources in finally block to ensure they're always closed
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}