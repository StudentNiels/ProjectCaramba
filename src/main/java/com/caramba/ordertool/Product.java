package com.caramba.ordertool;

public class Product {
    private String productNum;
    private String description;

    public Product(String productNum, String description) {
        this.productNum = productNum;
        this.description = description;
    }

    //region Getters and Setters
    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    //endregion
}
