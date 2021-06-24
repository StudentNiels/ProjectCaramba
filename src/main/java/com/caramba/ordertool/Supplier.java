package com.caramba.ordertool;


import java.util.UUID;

public class Supplier {
    private final String name;
    /**
     * Estimated delivery time in days
     */
    private final Integer avgDeliveryTime;
    /**
     * Products sold by this supplier
     */
    private final ProductList products = new ProductList();


    public Supplier(String name, int avgDeliveryTime) {
        this.name = name;
        this.avgDeliveryTime = avgDeliveryTime;
    }

    //region Getters and Setters
    public int getAvgDeliveryTime() {
        return avgDeliveryTime;
    }

    public String getName() {
        return name;
    }

    public ProductList getProducts() {
        return products;
    }

    public boolean containsProductWithKey(String k) {
        return products.containsKey(k);
    }

    //endregion

    public void addProduct(Product product) {
        String id = null;
        while (id == null || containsProductWithKey(id)) {
            //reroll key if there is a collision
            id = UUID.randomUUID().toString();
        }
        products.add(id, product);
    }

}