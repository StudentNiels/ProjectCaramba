package com.caramba.ordertool.models;


import java.util.UUID;

/**
 * A supplier were products are ordered form
 */
public class Supplier {
    private final String name;
    private final Integer avgDeliveryTime;
    /**
     *
     */
    private final ProductList products = new ProductList();

    /**
     * Constructor for supplier
     *
     * @param name            name of this supplier
     * @param avgDeliveryTime estimated delivery time in days
     */
    public Supplier(String name, int avgDeliveryTime) {
        this.name = name;
        this.avgDeliveryTime = avgDeliveryTime;
    }

    //region Getters and Setters

    /**
     * Returns estimated delivery time in days
     *
     * @return estimated delivery time in days
     */
    public int getAvgDeliveryTime() {
        return avgDeliveryTime;
    }

    /**
     * Returns the name of this supplier
     *
     * @return name of this supplier
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a ProductList with products sold by this supplier
     *
     * @return ProductList with products sold by this supplier
     */
    public ProductList getProducts() {
        return products;
    }

    /**
     * Returns true if this supplier offers the product with the specified productID
     *
     * @param productID the productID to test for
     * @return true if this supplier offers the product with the specified productID
     */
    public boolean containsProductWithKey(String productID) {
        return products.containsKey(productID);
    }

    //endregion

    /**
     * Add the specified product to the list of products offered by this supplier
     *
     * @param product product to add
     */
    public void addProduct(Product product) {
        String id = null;
        while (id == null || containsProductWithKey(id)) {
            //roll again if there is a collision
            id = UUID.randomUUID().toString();
        }
        products.add(id, product);
    }

}