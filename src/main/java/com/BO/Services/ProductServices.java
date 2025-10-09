package com.BO.Services;

import com.DB.supa;

import java.sql.Connection;
import java.sql.SQLException;

public class ProductServices {


    public static boolean isProductInStock(int productId) throws SQLException {
        String sql = "SELECT stock FROM product WHERE id = ? AND stock > 0";
        Connection conn = supa.getConnection();
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            var rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    public static void updateProductStock(int productId) throws SQLException {
        Connection conn = supa.getConnection();
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
