package com.caramba.ordertool.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;

/**
 * An recommended order for a certain supplier in preparation of sales in a certain year and month.
 */
public class Recommendation {
    private final LocalDateTime creationDate;
    private final Supplier supplier;
    private final YearMonth yearMonthToOrderFor;
    private final HashMap<Product, Integer> productRecommendation = new HashMap<>();
    private boolean confirmed = false;

    /**
     * Constructor for recommendation. The creation date will be set to the current local time.
     *
     * @param supplier            the supplier who should be ordered from
     * @param yearMonthToOrderFor the year and month for wich this recommendation is for. The products should arrive before this month in order to be prepared for the projected sales
     */
    public Recommendation(Supplier supplier, YearMonth yearMonthToOrderFor) {
        this.creationDate = LocalDateTime.now();
        this.supplier = supplier;
        this.yearMonthToOrderFor = yearMonthToOrderFor;
    }

    /**
     * Constructor for recommendation.
     *
     * @param supplier            the supplier who should be ordered from
     * @param yearMonthToOrderFor the year and month for wich this recommendation is for. The products should arrive before this month in order to be prepared for the projected sales
     * @param creationDate        LocalDateTime representing a timestamp of when this recommendation was generated
     */
    public Recommendation(Supplier supplier, YearMonth yearMonthToOrderFor, LocalDateTime creationDate) {
        this.creationDate = creationDate;
        this.supplier = supplier;
        this.yearMonthToOrderFor = yearMonthToOrderFor;
    }

    /**
     * Adds a specified amount of a certain product to the recommendation
     *
     * @param product the product to be ordered
     * @param amount  the amount of units that should be ordered
     */
    public void addProductToRecommendation(Product product, int amount) {
        this.productRecommendation.put(product, amount);
    }

    //region Getters and Setters

    /**
     * LocalDateTime representing a timestamp of when this recommendation was generated
     *
     * @return LocalDateTime of when this recommendation was generated
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * the supplier who should be ordered from
     *
     * @return the supplier
     */
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * Calculates the final possible order date for the products to arrive on the first of the month that this recommendation is in preparation for. This is based on the estimated average delivery time of the supplier
     *
     * @return LocalDate of the final order date
     */
    public LocalDate getFinalOrderDate() {
        return LocalDate.of(yearMonthToOrderFor.getYear(), yearMonthToOrderFor.getMonth(), 1).minusDays(supplier.getAvgDeliveryTime());
    }

    /**
     * the year and month for wich this recommendation is for. The products should arrive before this month in order to be prepared for the projected sales
     *
     * @return YearMonth that this recommendation is for.
     */
    public YearMonth getYearMonthToOrderFor() {
        return yearMonthToOrderFor;
    }

    /**
     * Returns a hashMap with the products to order as key, and the number of units to be bought as value.
     *
     * @return hashmap of products and quantities.
     */
    public HashMap<Product, Integer> getProductRecommendation() {
        return productRecommendation;
    }

    /**
     * Returns whether or not the recommendation was checked off by the user. Users can use this to keep track of orders they have already acted upon.
     *
     * @return true if the recommendation is checked off
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Whether or not the recommendation was checked off by the user. Users can use this to keep track of orders they have already acted upon.
     *
     * @param confirmed true to confirm the recommendation, false to unconfirm
     */
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
    //endregion
}
