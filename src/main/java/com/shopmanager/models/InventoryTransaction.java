package com.shopmanager.models;

import java.util.Date;

public class InventoryTransaction {
    private int id;
    private int productId;
    private int supplierId;
    private String transactionType;
    private int quantity;
    private double unitPrice;
    private double totalAmount;
    private Date transactionDate;
    private String notes;
    private Product product;
    private Supplier supplier;

    public InventoryTransaction() {
    }

    public InventoryTransaction(int id, int productId, int supplierId, String transactionType,
            int quantity, double unitPrice, Date transactionDate, String notes) {
        this.id = id;
        this.productId = productId;
        this.supplierId = supplierId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.transactionDate = transactionDate;
        this.notes = notes;
        calculateTotal();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotal();
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotal();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    private void calculateTotal() {
        this.totalAmount = this.quantity * this.unitPrice;
    }
}