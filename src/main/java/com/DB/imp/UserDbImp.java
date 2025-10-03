package com.DB.imp;

import com.BO.User;
import com.DB.supa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class UserDbImp extends User {

    public static Collection searchUser(String email, String username, String password) {
        return null;
    }

    private UserDbImp(int id, String email, String username, String password, int roleId) {
        super(id, email, username, password, roleId);
    }


    public static User findByEmail(String email) throws SQLException {
        Connection conn = supa.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT email FROM User LIMIT 1");
        return (User) rs;
    }

    public User findById(int id) {
        return null;
    }

    public static boolean insert(User user) throws SQLException {
        Connection conn = supa.getConnection();
        Statement stmt = conn.createStatement();

        String sql = "INSERT INTO users (email, username, password, role_id) " +
                "VALUES ('" + user.getEmail() + "', '" +
                user.getUsername() + "', '" +
                user.getPassword() + "', " +
                user.getRoleId() + ")";

        int rowsAffected = stmt.executeUpdate(sql);
        return rowsAffected > 0;
    }


    public boolean update(User user) {
        return false;
    }

    public boolean delete(int id) {
        return false;
    }
}
