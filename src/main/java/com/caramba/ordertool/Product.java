package com.caramba.ordertool;

public class Product {
    private String productNum;
    private String description;
    private Season season;

    public Product(String productNum, String description, Season season) {
        this.productNum = productNum;
        this.description = description;
        this.season = season;
    }

    public Product(String productNum, String description) {
        this(productNum, description, Season.UNDEFINED);
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

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    //endregion
}
