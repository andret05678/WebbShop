package com.DB.imp;

import com.BO.User;
import com.DB.supa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

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

        // FIXED: Select all columns and use WHERE clause with proper table name
        String sql = "SELECT id, email, username, password, role_id FROM users WHERE email = '" + email + "'";
        ResultSet rs = stmt.executeQuery(sql);

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
        Statement stmt = conn.createStatement();

        // FIXED: Removed token field and fixed SQL syntax
        String sql = "INSERT INTO users (email, username, password, role_id) " +
                "VALUES ('" + user.getEmail() + "', '" +
                user.getUsername() + "', '" +
                user.getPassword() + "', " +
                user.getRoleId() + ")";

        System.out.println("Executing SQL: " + sql); // Debug line

        int rowsAffected = stmt.executeUpdate(sql);

        // Close resources
        stmt.close();
        conn.close();

        return rowsAffected > 0;
    }

    // Alternative: Using PreparedStatement (recommended)
    public static boolean insertSecure(User user) throws SQLException {
        Connection conn = supa.getConnection();

        // FIXED: Use PreparedStatement to prevent SQL injection
        String sql = "INSERT INTO users (email, username, password, role_id) VALUES (?, ?, ?, ?)";
        java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, user.getEmail());
        pstmt.setString(2, user.getUsername());
        pstmt.setString(3, user.getPassword());
        pstmt.setInt(4, user.getRoleId());

        int rowsAffected = pstmt.executeUpdate();

        // Close resources
        pstmt.close();
        conn.close();

        return rowsAffected > 0;
    }

    public User findById(int id) {
        return null;
    }

    public boolean update(User user, int id) {
        return false;
    }

    public boolean delete(int id) {
        return false;
    }

    // Factory method to create UserDbImp instances
    public static User createUserInstance(String email, String username, String password, int roleId) {
        return new UserDbImp(0, email, username, password, roleId);
    }
}