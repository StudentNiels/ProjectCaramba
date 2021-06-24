package com.caramba.ordertool.models;

/**
 * A product that's being tracked by the OrderTool.
 */
public class Product {
    private String productNum;
    private String description;
    private int quantity;

    /**
     * Constructor for product.
     *
     * @param productNum  the product number that is used within the companies administration. Note: This is not the same as the internal ID used by the orderTool.
     * @param description a description in human readable text. Usually describes the name of the product.
     */
    public Product(String productNum, String description) {
        this.productNum = productNum;
        this.description = description;
        this.quantity = 0;
    }

    /**
     * Empty constructor to automatically serialize from firebase
     */
    public Product(){

    }

    //region Getters and Setters

    /**
     * Returns the product number that is used within the companies administration. Note: This is not the same as the internal ID used by the orderTool.
     *
     * @return the product number
     */
    public String getProductNum() {
        return productNum;
    }

    /**
     * Returns a description in human readable text. Usually describes the name of the product.
     *
     * @return the product description
     */
    public String getDescription() {
        return description;
    }

    /**
     * The amount of units of this product that are currently in stock.
     *
     * @return quantity currently in stock
     */
    public int getQuantity() {
        return quantity;
    }

    //endregion

}
