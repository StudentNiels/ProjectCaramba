package com.caramba.ordertool;

public class Product {
    private String productNum;
    private String description;
    private Season season;

    public Product(String productNum, String description) {
        this.productNum = productNum;
        this.description = description;
        this.season = Season.UNDEFINED;
    }

    public Product(String productNum, String description, String season) {
        this.productNum = productNum;
        this.description = description;
        this.season = setSeason(season);
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

    public Season setSeason(String season) {
        Season returnValue = null;
        if(season.equals("zomer")){
            returnValue = Season.SUMMER;
        }
        if(season.equals("winter")){
            returnValue = Season.WINTER;
        }
        if(season.equals("herfst")){
            returnValue = Season.FALL;
        }
        if(season.equals("lente")){
            returnValue = Season.SPRING;
        }
        if(season.equals("allround")){
            returnValue = Season.ALLROUND;
        }
        return returnValue;
    }

    //endregion
}
