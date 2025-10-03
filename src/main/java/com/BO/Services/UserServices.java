package com.BO.Services;

import com.BO.PasswordUtil;
import com.BO.User;
import com.DB.imp.UserDbImp;


import java.sql.SQLException;

import static com.DB.imp.UserDbImp.findByEmail;

public class UserServices {

    public UserServices() {
    }

    public User register (String email, String password)
    {

    }

    public User login(String email, String passwordPlain) {
        try {
            User testUser = findByEmail(email); // should return salt + hash
            if (testUser != null) {
                if (PasswordUtil.verifyPassword(passwordPlain, testUser.getSalt(), testUser.getPassword())) {
                    return testUser;
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

