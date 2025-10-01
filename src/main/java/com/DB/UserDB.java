package com.DB;

import com.Model.User;

import java.sql.SQLException;

public interface UserDB {
    User findByEmail(String email) throws SQLException;
    User findById(int id);
    User insert(User user);
    boolean update(User user);
    boolean delete(int id);
}
