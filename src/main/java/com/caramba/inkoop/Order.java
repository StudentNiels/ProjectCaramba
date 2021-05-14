package com.caramba.inkoop;

import java.util.Date;
import java.util.HashMap;

public class Order {

    private Date bestelDatum;
    private Date factuurDatum;
    private HashMap<Product, Integer> shoppingCart;
    // TODO: Leverancier opvangen voor de inkopen


    public Order(Date bestelDatum, Date factuurDatum, HashMap<Product, Integer> shoppingCart) {
        this.bestelDatum = bestelDatum;
        this.factuurDatum = factuurDatum;
        this.shoppingCart = shoppingCart;
    }

    //region Getters and Setters
    public Date getBestelDatum() {
        return bestelDatum;
    }

    public void setBestelDatum(Date bestelDatum) {
        this.bestelDatum = bestelDatum;
    }

    public Date getFactuurDatum() {
        return factuurDatum;
    }

    public void setFactuurDatum(Date factuurDatum) {
        this.factuurDatum = factuurDatum;
    }

    public HashMap<Product, Integer> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(HashMap<Product, Integer> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
    //endregion

    public void addToShoppingCart(Product product, int amount){
        if(shoppingCart.containsKey(product)){
            shoppingCart.replace(product,(shoppingCart.get(product) + amount));
        }
        else{
            shoppingCart.put(product, amount);
        }
    }
}
