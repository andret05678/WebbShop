package com.UI;

public class ProductInfo {
    private String name;
    private String description;
    private String price;
    private String category;
    private String categoryId;
    private String categoryName;
    private String categoryDescription;

    public ProductInfo(String name, String description, String price, String category, String categoryId, String categoryName, String categoryDescription) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {}
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {}
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {}
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {}
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {}
    public String getCategoryName() {
        return categoryName;
    }

}
