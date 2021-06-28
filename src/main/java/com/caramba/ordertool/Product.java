package com.caramba.ordertool;

public class Product {
    private String productNum;
    private String description;
    private int quantity;

    public Product(String productNum, String description) {
        this.productNum = productNum;
        this.description = description;
        this.quantity = 0;
    }

    public Product(){

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
