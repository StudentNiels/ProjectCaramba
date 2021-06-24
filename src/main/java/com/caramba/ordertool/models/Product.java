package com.caramba.ordertool.models;

public class Product {
    private String productNum;
    private String description;
    private int quantity;

    public Product(String productNum, String description) {
        this.productNum = productNum;
        this.description = description;
        this.quantity = 0;
    }

    public Product() {

    }

    //region Getters and Setters
    public String getProductNum() {
        return productNum;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    //endregion

}
