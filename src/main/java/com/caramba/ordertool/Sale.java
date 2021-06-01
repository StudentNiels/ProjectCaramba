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

    public Sale(HashMap<Product, Integer> products) {
        this.date = null;
        this.products = products;
    }

    //region Getters and Setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Product, Integer> products) {
        this.products = products;
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