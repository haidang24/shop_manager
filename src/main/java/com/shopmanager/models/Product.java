package com.shopmanager.models;

public class Product {
    private int id;
    private String name;
    private int categoryId;
    private double price;
    private int quantity;
    private String size;
    private String color;
    private String imageUrl;
    private String description;
    private Category category;

    public Product() {
    }

    public Product(int id, String name, int categoryId, double price, int quantity, String size, String color,
            String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        if (category != null) {
            this.categoryId = category.getId();
        }
    }

    @Override
    public String toString() {
        return name;
    }
}