package com.caramba.ordertool;

import java.util.List;

public class Product {
    private String product_descript;
    private TimePeriod timePeriod;
    private TimePeriodController tpc = new TimePeriodController();
    private String product_num;
    private int min_supply;
    private int supply;

    public Product(String product_descript, String product_num, int min_supply, int supply) {
        FireStoreConfig fireStoreConfig = new FireStoreConfig();
        fireStoreConfig.dbConnect();
        fireStoreConfig.setupProductDocument(this.product_descript = product_descript, this.timePeriod = null, this.product_num = product_num, this.min_supply = min_supply,this.supply = supply);
    }

    public Product(String product_num, String product_descript, String timePeriod, int min_supply, int supply) {
        this.product_num = product_num;
        this.product_descript = product_descript;
        this.timePeriod = tpc.getTimePeriodByString(timePeriod);
        this.supply = supply;
        this.min_supply = min_supply;
    }

    //region Getters and Setters
    public String getProduct_num() {
        return product_num;
    }

    public void setProduct_num(String product_num) {
        this.product_num = product_num;
    }

    public String getProduct_descript() {
        return product_descript;
    }

    public void setProduct_descript(String product_descript) {
        this.product_descript = product_descript;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public int getMin_supply() {
        return min_supply;
    }

    public void setMin_supply(int min_supply) {
        this.min_supply = min_supply;
    }

    public int getSupply() {
        return supply;
    }

    public void setSupply(int supply) {
        this.supply = supply;
    }
    //endregion
}
