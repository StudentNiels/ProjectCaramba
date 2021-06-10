package com.caramba.ordertool;

import java.util.Date;
import java.util.HashMap;

public class Recommendation {
    private Date creationDate;
    private Supplier supplier;
    private Date finalOrderDate;
    private HashMap<Product, Integer> productRecommendation;
    private boolean confirmed;

    public Recommendation() {
        this.creationDate = null;
        this.supplier = null;
        this.finalOrderDate = null;
        this.productRecommendation = new HashMap<>();
        this.confirmed = false;
    }

    public Recommendation(Date creationDate, Supplier supplier, Date finalOrderDate, HashMap<Product, Integer> productRecommendation, boolean confirmed) {
        this.creationDate = creationDate;
        this.supplier = supplier;
        this.finalOrderDate = finalOrderDate;
        this.productRecommendation = productRecommendation;
        this.confirmed = confirmed;
    }

    //region Getters and Setters
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Date getFinalOrderDate() {
        return finalOrderDate;
    }

    public void setFinalOrderDate(Date finalOrderDate) {
        this.finalOrderDate = finalOrderDate;
    }

    public HashMap<Product, Integer> getProductRecommendation() {
        return productRecommendation;
    }

    public void setProductRecommendation(HashMap<Product, Integer> productRecommendation) {
        this.productRecommendation = productRecommendation;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
    //endregion
}
