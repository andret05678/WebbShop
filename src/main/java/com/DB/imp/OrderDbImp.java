package com.DB.imp;
import com.BO.Order;
import com.DB.supa;
import com.UI.Info.OrderInfo;

import java.sql.*;

public class OrderDbImp extends Order {

    private OrderDbImp(int id, int userId, String status) {
        super(id, userId, status);
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


}
