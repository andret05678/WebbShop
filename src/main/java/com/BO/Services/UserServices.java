package com.BO.Services;

import com.BO.PasswordUtil;
import com.BO.User;
import com.DB.imp.UserDbImp;
import java.sql.SQLException;
import java.util.UUID;


public class UserServices {

    public UserServices() {
    }

    public User register(String email, String password, String username, int roleId) {
        try {
            User existingUser = UserDbImp.findByEmail(email);
            if (existingUser != null) {
                return null; // User already exists
            }

            String saltedHash = PasswordUtil.generateSaltedHash(password);
            String token = UUID.randomUUID().toString();

            User newUser = User.createUser(0, email, username, saltedHash, roleId,token);
            boolean inserted = UserDbImp.insert(newUser);

            return inserted ? newUser : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User login(String email, String passwordPlain) {
        try {
            User testUser = UserDbImp.findByEmail(email);
            if (testUser != null) {
                if (PasswordUtil.verifyPassword(passwordPlain, testUser.getPassword())) {
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