package com.caramba.ordertool;

public class Product {
    private String productNum;
    private String description;
    private TimePeriod period;
    private TimePeriodController tpc = new TimePeriodController();

    public Product(String productNum, String description) {
        this.productNum = productNum;
        this.description = description;
        this.period = null;
    }

    public Product(String productNum, String description, String season) {
        this.productNum = productNum;
        this.description = description;
        this.period = tpc.getTimePeriodByString(season);
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

    public TimePeriod getSeason() {
        return period;
    }

    public void setSeason(String season) {
        period = tpc.getTimePeriodByString(season);
    }
    //endregion
}
