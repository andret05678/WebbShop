package com.UI;

import com.BO.Product;

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
    public void setName(String name) {}
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {}
    public int getPrice() {
        return price;
    }
    public void setPrice(String price) {}
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {}
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {}
    public String getCategoryName() {
        return categoryName;
    }

}
