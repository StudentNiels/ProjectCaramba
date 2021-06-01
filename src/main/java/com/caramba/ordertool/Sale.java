package com.caramba.ordertool;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Sale {

    private Date date;
    private HashMap<Product, Integer> products;
    // TODO: Leverancier opvangen voor de inkopen

    public Sale(){
        this.date = null;
        this.products = new HashMap<>();
    }

    public Sale(HashMap<Product, Integer> shoppingCart) {
        this.date = null;
        this.products = shoppingCart;
    }

    //region Getters and Setters
    public Date getBestelDatum() {
        return date;
    }

    public void setBestelDatum(Date bestelDatum) {
        this.date = bestelDatum;
    }

    public HashMap<Product, Integer> getShoppingCart() {
        return products;
    }

    public void setShoppingCart(HashMap<Product, Integer> shoppingCart) {
        this.products = shoppingCart;
    }
    //endregion

    public void addToProducts(Product product, int amount){
        if(products.containsKey(product)){
            products.replace(product,(products.get(product) + amount));
        }
        else{
            products.put(product, amount);
        }
    }

    public void listProducts(){
        System.out.println("Saleslist items:");
        for(Map.Entry<Product, Integer> product : this.products.entrySet()){
            Product item = product.getKey();
            int amount = product.getValue();

            System.out.println(item.getProductNum() + " " + item.getDescription() + "/" + amount);
        }
    }
}