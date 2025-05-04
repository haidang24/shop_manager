package com.shopmanager.models;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Order {
    private int id;
    private int customerId;
    private Date orderDate;
    private double totalAmount;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private String shippingAddress;
    private double shippingFee;
    private String notes;
    private List<OrderItem> orderItems;
    private Customer customer;

    public Order() {
        orderItems = new ArrayList<>();
    }

    public Order(int id, int customerId, Date orderDate, double totalAmount, String status,
            String paymentMethod, String paymentStatus, String shippingAddress,
            double shippingFee, String notes) {
        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.shippingAddress = shippingAddress;
        this.shippingFee = shippingFee;
        this.notes = notes;
        this.orderItems = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        calculateTotal();
    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        calculateTotal();
    }

    private void calculateTotal() {
        double total = 0;
        for (OrderItem item : orderItems) {
            total += item.getSubtotal();
        }
        this.totalAmount = total + shippingFee;
    }
}