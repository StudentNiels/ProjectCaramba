package com.caramba.ordertool;

public class Product {
    private String productNum;
    private String description;
    private int quantity;
    private int minQuantity;

    public Product(String productNum, String description, int minQuantity) {
        this.productNum = productNum;
        this.description = description;
        this.quantity = 0;
        this.minQuantity = minQuantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }
    //endregion

    public void decrease(){
        this.quantity--;
    }

    public void decrease(int amount){
        this.quantity -= amount;
    }

    public void increase(int amount){
        this.quantity =+ amount;
    }
}
