package com.DB.imp;

import com.BO.User;
import com.DB.supa;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDbImp extends User {

    public static Collection searchUser(String email, String username, String password, String token) {
        return null;
    }

    private UserDbImp(int id, String email, String username, String password, int roleId) {
        super(id, email, username, password, roleId);
    }

    public static User findByEmail(String email) throws SQLException {
        Connection conn = supa.getConnection();
        Statement stmt = conn.createStatement();

        String sql = "SELECT id, email, username, password, role_id FROM users WHERE email = '" + email + "'";
        ResultSet rs = stmt.executeQuery(sql);

        conn.close();
        if (rs.next()) {
            return User.createUser(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getInt("role_id")
            );
        }
        return null;
    }

    public static boolean insert(User user) throws SQLException {
        Connection conn = supa.getConnection();

        String sql = "INSERT INTO users (email, username, password, role_id) VALUES (?, ?, ?, ?)";
        java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, user.getEmail());
        pstmt.setString(2, user.getUsername());
        pstmt.setString(3, user.getPassword());
        pstmt.setInt(4, user.getRoleId());

        int rowsAffected = pstmt.executeUpdate();

        pstmt.close();
        conn.close();

        return rowsAffected > 0;
    }

    public static List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        Connection conn = supa.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, email, username, password, role_id FROM users ORDER BY id");

        while (rs.next()) {
            User user = User.createUser(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getInt("role_id")
            );
            users.add(user);
        }

        rs.close();
        stmt.close();
        conn.close();

        return users;
    }

    public static boolean updateUserRole(int userId, int newRoleId) throws SQLException {
        Connection conn = supa.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET role_id = ? WHERE id = ?");

        pstmt.setInt(1, newRoleId);
        pstmt.setInt(2, userId);

        int rowsAffected = pstmt.executeUpdate();

        pstmt.close();
        conn.close();

        return rowsAffected > 0;
    }

    public static boolean deleteUser(int userId) throws SQLException {
        Connection conn = supa.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");

        pstmt.setInt(1, userId);

        int rowsAffected = pstmt.executeUpdate();

        pstmt.close();
        conn.close();

        return rowsAffected > 0;
    }

}