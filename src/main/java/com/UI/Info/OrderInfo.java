package com.UI.Info;

public class OrderInfo {
    private int id;
    private int userId;
    private String status;

    public OrderInfo(int id, int userId, String status) {
        this.id = id;
        this.userId = userId;
        this.status = status;
    }

    public int getId() {
        return id;
    }
    public int getUserId(){
        return userId;
    }
    public String getStatus(){
        return status;
    }
}
