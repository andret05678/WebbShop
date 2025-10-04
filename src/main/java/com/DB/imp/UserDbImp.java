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

    private UserDbImp(int id, String email, String username, String password, int roleId,String token) {
        super(id, email, username, password, roleId, token);
    }


    public static User findByEmail(String email) throws SQLException {
        Connection conn = supa.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT email FROM User LIMIT 1");
        if (rs.next()) {
            return  User.createUser(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getInt("role_id"),
                    rs.getString("token")
            );
        }
        return null;
    }

    public User findById(int id) {
        return null;
    }

    public static boolean insert(User user) throws SQLException {
        Connection conn = supa.getConnection();
        Statement stmt = conn.createStatement();

        String sql = "INSERT INTO user (email, username, password, role_id, token) " +
                "VALUES ('" + user.getEmail() + "', '" +
                user.getUsername() + "', '" +
                user.getPassword() + "', " +
                user.getRoleId() + "', ''" + user.getToken() + "')";


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
