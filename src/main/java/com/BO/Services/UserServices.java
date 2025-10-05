package com.BO.Services;

import com.BO.PasswordUtil;
import com.BO.User;
import com.UI.Info.UserInfo;
import com.DB.imp.UserDbImp;

import java.sql.SQLException;

public class UserServices {

    public UserServices() {
    }

    public UserInfo register(String email, String password, String username, int roleId) {
        try {
            User existingUser = UserDbImp.findByEmail(email);
            if (existingUser != null) {
                return null; // User already exists
            }

            String saltedHash = PasswordUtil.generateSaltedHash(password);

            User newUser = User.createUser(0, email, username, saltedHash, roleId);
            boolean inserted = UserDbImp.insert(newUser);

            if (inserted) {
                // Fetch the newly created user to get the actual ID
                User createdUser = UserDbImp.findByEmail(email);
                return new UserInfo(createdUser.getId(), createdUser.getUsername(), createdUser.getRoleId());
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserInfo login(String email, String passwordPlain) {
        try {
            User testUser = UserDbImp.findByEmail(email);
            if (testUser != null) {
                if (PasswordUtil.verifyPassword(passwordPlain, testUser.getPassword())) {
                    // Convert User to UserInfo for the session
                    return new UserInfo(testUser.getId(), testUser.getUsername(), testUser.getRoleId());
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}