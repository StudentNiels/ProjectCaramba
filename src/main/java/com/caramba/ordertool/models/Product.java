package com.caramba.ordertool.models;

public class Product {
    private final String productNum;
    private final String description;
    private final int quantity;

    public Product(String productNum, String description) {
        this.productNum = productNum;
        this.description = description;
        this.quantity = 0;
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
