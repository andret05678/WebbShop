package com.UI.Info;

public class UserInfo {
    private int id;
    private String username;
    private int role;
    private String email;


    public UserInfo(int id, String username, int role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }
}
