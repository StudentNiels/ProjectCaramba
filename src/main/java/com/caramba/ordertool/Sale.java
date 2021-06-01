package com.caramba.ordertool;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.*;

public class Sale {

    private String order_nr;
    private LocalDate date;
    private HashMap<UUID, Integer> products;

    public Sale(){
        this.date = LocalDate.now();
        this.products = new HashMap<>();
    }

    public Sale(LocalDate date) {
        this.date = LocalDate.now();
        this.products = new HashMap<>();
    }

    public Sale(HashMap<UUID, Integer> products) {
        this.date = LocalDate.now();
        this.products = products;
    }

    public Sale(HashMap<UUID, Integer> products, LocalDate date) {
        this.date = date;
        this.products = products;
    }

    //region Getters and Setters
    public int getAmountByID(UUID uuid) throws InvalidParameterException{
        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            if(entry.getKey().equals(uuid)){
                return entry.getValue();
            }
        }
        throw new InvalidParameterException("This is not a valid UUID");
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public HashMap<UUID, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<UUID, Integer> products) {
        this.products = products;
    }
    //endregion

    public void addToProducts(UUID uuid, int amount){
        if(products.containsKey(uuid)){
            products.replace(uuid,(products.get(uuid) + amount));
        }
        else{
            products.put(uuid, amount);
        }
    }

    public void listProducts(){
        System.out.println("Saleslist items:");
        HashMap<UUID, Product> allProducts = Application.getMainProductList().getProducts();
        Product selectedProduct = null;

        for(Map.Entry<UUID, Integer> product : this.products.entrySet()){
            UUID productUUID = product.getKey();
            int amount = product.getValue();

            if(allProducts.containsKey(productUUID)){
                selectedProduct = allProducts.get(productUUID);
            }

            System.out.println(selectedProduct.getProductNum() + " " + selectedProduct.getDescription() + "/" + amount);
        }
    }
}