package com.DB;

import com.Model.User;

public interface UserDB {
    User findByEmail(String email);
    User findById(int id);
    User insert(User user);
    boolean update(User user);
    boolean delete(int id);
}
