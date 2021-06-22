package com.caramba.ordertool;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;

public class Recommendation {
    private LocalDateTime creationDate;
    private Supplier supplier;
    //TODO: find a use for this or delete this when the time is ripe
    private final YearMonth yearMonthToOrderFor;
    private HashMap<Product, Integer> productRecommendation = new HashMap<>();
    private boolean confirmed = false;

    public Recommendation(Supplier supplier, YearMonth yearMonthToOrderFor) {
        this.creationDate = LocalDateTime.now();
        this.supplier = supplier;
        this.yearMonthToOrderFor = yearMonthToOrderFor;
    }

    public Recommendation(Supplier supplier, YearMonth yearMonthToOrderFor, LocalDateTime creationDate) {
        this.creationDate = creationDate;
        this.supplier = supplier;
        this.yearMonthToOrderFor = yearMonthToOrderFor;
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

    public LocalDate getFinalOrderDate() {
        return LocalDate.of(yearMonthToOrderFor.getYear(), yearMonthToOrderFor.getMonth(), 1).minusDays(supplier.getAvgDeliveryTime());
    }

    public YearMonth getYearMonthToOrderFor() {
        return yearMonthToOrderFor;
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
