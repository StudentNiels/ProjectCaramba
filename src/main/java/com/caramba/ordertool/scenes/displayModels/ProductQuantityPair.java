package com.caramba.ordertool.scenes.displayModels;

/**
 * Model used by the products table in the recommended orders view
 */
//getters are used by javafx, but IntelliJ doesn't recognize this, so we suppress the warnings
@SuppressWarnings("unused")
public class ProductQuantityPair {
    final String productNum;
    final String productDescription;
    final int quantity;

    public ProductQuantityPair(String productNum, String productDescription, int quantity) {
        this.productNum = productNum;
        this.productDescription = productDescription;
        this.quantity = quantity;
    }

    public String getProductNum() {
        return productNum;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getQuantity() {
        return quantity;
    }
}
