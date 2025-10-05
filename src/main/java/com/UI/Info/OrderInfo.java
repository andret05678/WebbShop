package com.UI.Info;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class OrderInfo {
    private int id;
    private int userId;
    private String userEmail;
    private String status;
    private BigDecimal totalAmount;
    private Timestamp orderDate;
    private List<OrderItemInfo> items;

<<<<<<< Updated upstream
    public OrderInfo(int id, int userId, String status) {
        this.id = id;
        this.userId = userId;
        this.status = status;
    }

    public int getId() {
        return id;
=======
    public OrderInfo(int id, int userId, String userEmail, String status, BigDecimal totalAmount, Timestamp orderDate) {
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
>>>>>>> Stashed changes
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getUserEmail() { return userEmail; }
    public String getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public Timestamp getOrderDate() { return orderDate; }
    public List<OrderItemInfo> getItems() { return items; }

    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setItems(List<OrderItemInfo> items) { this.items = items; }
}