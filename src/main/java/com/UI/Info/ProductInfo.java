package com.UI.Info;

public class ProductInfo {
    private int id;
    private String name;
    private String description;
    private int price;
    private String category;
    private int categoryId;
    private String categoryName;
    private String categoryDescription;

    public ProductInfo(int id,String name, String description, int price, String category, int categoryId, String categoryName, String categoryDescription) {
        this.id =id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
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

    public int getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }
}
