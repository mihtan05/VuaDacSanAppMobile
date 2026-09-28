package com.example.baicuoiki.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private String image;
    private String description;
    private String unit;
    private double price;
    private int stock;
    private String expiryDate;
    private String status;
    private String supplierId;
    private String supplierName; // Thêm tên nhà cung cấp

    public Product() {}

    public Product(String id, String name, String image, String description, String unit, double price, int stock, String expiryDate, String status, String supplierId) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.unit = unit;
        this.price = price;
        this.stock = stock;
        this.expiryDate = expiryDate;
        this.status = status;
        this.supplierId = supplierId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
}