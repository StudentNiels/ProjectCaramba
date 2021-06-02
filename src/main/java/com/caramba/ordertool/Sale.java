package com.caramba.ordertool;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.*;

public class Sale {

    private String order_nr;
    private LocalDate date;
    private HashMap<String, Integer> products;

    public Sale(LocalDate date) {
        this.date = date;
        this.products = new HashMap<>();
    }

    public Sale(HashMap<String, Integer> products, LocalDate date) {
        this.date = date;
        this.products = products;
    }

    //region Getters and Setters
    public int getAmountByID(String uuid) throws InvalidParameterException{
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            if(entry.getKey().equals(uuid)){
                return entry.getValue();
            }
        }
        throw new InvalidParameterException("This is not a valid id");
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public HashMap<String, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<String, Integer> products) {
        this.products = products;
    }
    //endregion

    public void addToProducts(String uuid, int amount){
        if(products.containsKey(uuid)){
            products.replace(uuid,(products.get(uuid) + amount));
        }
        else{
            products.put(uuid, amount);
        }
    }

    public void listProducts(){
        System.out.println("Saleslist items:");
        HashMap<String, Product> allProducts = Application.getMainProductList().getProducts();
        Product selectedProduct = null;

        for(Map.Entry<String, Integer> product : this.products.entrySet()){
            String productID = product.getKey();
            int amount = product.getValue();

            if(allProducts.containsKey(productID)){
                selectedProduct = allProducts.get(productID);
            }

            System.out.println(selectedProduct.getProductNum() + " " + selectedProduct.getDescription() + "/" + amount);
        }
    }
}