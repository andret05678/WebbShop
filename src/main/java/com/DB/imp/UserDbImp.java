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

    protected UserDbImp(int id, String email, String username, String password, int roleId) {
        super(id, email, username, password, roleId);
    }


    public User findByEmail(String email) throws SQLException {
        Connection conn = supa.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT email FROM User LIMIT 1");
        return (User) rs;
    }

    public User findById(int id) {
        return null;
    }

    public User insert(User user) {
        return null;
    }

    public boolean update(User user) {
        return false;
    }

    public boolean delete(int id) {
        return false;
    }
}
