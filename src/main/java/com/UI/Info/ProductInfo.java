package com.UI.Info;

public class ProductInfo {
    private int id;
    private String name;
    private String description;
    private double price;
    private int categoryId;
    private int stock;

    public ProductInfo(int id,String name, String description, double price, int categoryId,int stock) {
        this.id =id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.stock = stock;

    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getCategoryId() {
        return categoryId;
    }
    public int getStock() {
        return stock;
    }

}
