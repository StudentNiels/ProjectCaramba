package com.caramba.ordertool.models;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * A sale made by the company. Used to calculate product performance and projected sales.
 */
public class Sale {

    private final LocalDateTime date;
    private final HashMap<String, Integer> products;

    public Sale(LocalDateTime date) {
        this.date = date;
        this.products = new HashMap<>();
    }

    /**
     * Constructor for sale
     *
     * @param products hashmap with the id's of the products that were sold as key, and the amount of units sold as value
     * @param date     localDateTime of when this sale was made
     */
    public Sale(HashMap<String, Integer> products, LocalDateTime date) {
        this.date = date;
        this.products = products;
    }

    //region Getters and Setters

    /**
     * Returns amount of units sold of the product corresponding to the specified product id
     *
     * @param productID the id of the product
     * @return the amount of units sold of the specified product during this sale
     * @throws InvalidParameterException if this productID is not in this sale
     */
    public int getAmountByID(String productID) throws InvalidParameterException {
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            if (entry.getKey().equals(productID)) {
                return entry.getValue();
            }
        }
        throw new InvalidParameterException("This is not a valid id");
    }

    /**
     * Returns localDateTime of when this sale was made
     *
     * @return localDateTime of when this sale was made
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * returns a hashmap with product id's as key and amount of units sold during this sale as value
     *
     * @return hashmap with product id's as key and amount of units sold during this sale as value
     */
    public HashMap<String, Integer> getProducts() {
        return products;
    }

    //endregion

    /**
     * Add the specified product id and amount sold to this sale
     *
     * @param productID the id of the product sold during this sale
     * @param amount    the amount of units of the product sold during this sale
     */
    public void addToProducts(String productID, int amount) {
        if (products.containsKey(productID)) {
            products.replace(productID, (products.get(productID) + amount));
        } else {
            products.put(productID, amount);
        }
    }
}