package com.caramba.ordertool;

public class Product {
    private String productNum;
    private String description;
    private TimePeriod period;
    private TimePeriodController tpc = new TimePeriodController();
    private int quantity;
    private int minQuantity;

    public Product(String productNum, String description, int minQuantity) {
        this.productNum = productNum;
        this.description = description;
        this.period = null;
        this.quantity = 0;
        this.minQuantity = minQuantity;
    }

    public Product(String productNum, String description, String season, int minQuantity) {
        this.productNum = productNum;
        this.description = description;
        this.period = tpc.getTimePeriodByString(season);
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

    public TimePeriod getPeriod() {
        return period;
    }

    public void setPeriod(String season) {
        period = tpc.getTimePeriodByString(season);
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
