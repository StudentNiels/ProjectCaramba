package com.caramba.inkoop;

public class Product {

    private String articlenr;
    private String description;
    private Season season;
    /* Probably more variables needed, but this is purely for testing */

    public Product(String articlenr, String description) {
        this.articlenr = articlenr;
        this.description = description;
        this.season = Season.UNDEFINED;
    }

    public Product(String articlenr, String description, Season season) {
        this.articlenr = articlenr;
        this.description = description;
        this.season = season;
    }

    public String getArticlenr() {
        return articlenr;
    }

    public void setArticlenr(String articlenr) {
        this.articlenr = articlenr;
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
}
