package com.UI.Info;

import java.math.BigDecimal;

public class OrderItemInfo {
    private int id;
    private int orderId;
    private int productId;
    private String productName;
    private int quantity;
    private double price;

    public OrderItemInfo(int id, int orderId, int productId, String productName, int quantity, double price) {
        this.id = -1;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() { return id; }
    public int getOrderId() { return orderId; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}