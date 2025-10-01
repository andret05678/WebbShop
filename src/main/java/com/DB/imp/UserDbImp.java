package com.DB.imp;

import com.Model.User;
import com.SUPAUTIL.supa;

import java.sql.SQLException;

public class UserDbImp implements com.DB.UserDB {

    @Override
    public User findByEmail(String email) throws SQLException {
        supa.getConnection();
    }

    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public User insert(User user) {
        return null;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
