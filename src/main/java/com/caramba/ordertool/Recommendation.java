package com.caramba.ordertool;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

public class Recommendation {
    private LocalDateTime creationDate;
    private Supplier supplier;
    //TODO: find a use for this or delete this when the time is ripe
    private LocalDateTime finalOrderDate;
    private HashMap<Product, Integer> productRecommendation;
    private boolean confirmed;

    public Recommendation() {
        this.creationDate = null;
        this.supplier = null;
        this.finalOrderDate = null;
        this.productRecommendation = new HashMap<>();
        this.confirmed = false;
    }

    public Recommendation(LocalDateTime creationDate, Supplier supplier, HashMap<Product, Integer> productRecommendation) {
        this.creationDate = creationDate;
        this.supplier = supplier;
        this.finalOrderDate = null;
        this.productRecommendation = productRecommendation;
        this.confirmed = false;
    }

    public Recommendation(LocalDateTime creationDate, Supplier supplier, HashMap<Product, Integer> productRecommendation, boolean confirmed) {
        this.creationDate = creationDate;
        this.supplier = supplier;
        this.finalOrderDate = null;
        this.productRecommendation = productRecommendation;
        this.confirmed = confirmed;
    }

    public void addProductToRecommendation(Product product, int amount){
        this.productRecommendation.put(product, amount);
    }

    //region Getters and Setters
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public LocalDateTime getFinalOrderDate() {
        return finalOrderDate;
    }

    public void setFinalOrderDate(LocalDateTime finalOrderDate) {
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
