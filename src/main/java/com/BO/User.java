package com.BO;

import com.DB.imp.UserDbImp;

import java.util.Base64;
import java.util.Collection;

public class User {
    private int id;
    private String email;
    private String username;
    private String password;
    private int roleId;
    private String token;

    static public Collection searchUser(String email, String username, String password, String token) {
        return UserDbImp.searchUser(email, username, password,token);
    }

    public static User createUser(int id, String email, String username, String password, int roleId, String token) {
        return new User(id, email, username, password, roleId, token);
    }

    protected User(int id, String email, String username, String password, int roleId, String token) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
