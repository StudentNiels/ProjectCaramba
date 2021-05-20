package com.caramba.ordertool;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {

    private Date bestelDatum;
    private Date factuurDatum;
    private HashMap<Product, Integer> shoppingCart;
    // TODO: Leverancier opvangen voor de inkopen

    public Order(){
        this.bestelDatum = null;
        this.factuurDatum = null;
        this.shoppingCart = new HashMap<>();
    }

    public Order(HashMap<Product, Integer> shoppingCart) {
        this.bestelDatum = null;
        this.factuurDatum = null;
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

    public void listShoppingCart(){
        System.out.println("Orderlist items:");
        for(Map.Entry<Product, Integer> product : this.shoppingCart.entrySet()){
            Product item = product.getKey();
            int amount = product.getValue();

            System.out.println(item.getProductNum() + " " + item.getDescription() + "/" + amount);
        }
    }
}