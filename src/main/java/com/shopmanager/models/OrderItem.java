package com.shopmanager.models;

public class OrderItem {
    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    private double price;
    private double subtotal;
    private Product product;

    public OrderItem() {
    }

    public OrderItem(int id, int orderId, int productId, int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        calculateSubtotal();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        calculateSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    private void calculateSubtotal() {
        this.subtotal = this.price * this.quantity;
    }
}