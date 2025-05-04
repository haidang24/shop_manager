package com.shop.models;

public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String size;
    private String color;

    public Product() {
    }

    public Product(int id, String name, String category, double price, int quantity, String size, String color) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.color = color;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format("Product[id=%d, name='%s', category='%s', price=%.2f, quantity=%d, size='%s', color='%s']",
                id, name, category, price, quantity, size, color);
    }
}