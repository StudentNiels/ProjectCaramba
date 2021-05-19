package com.caramba.ordertool;

import java.util.HashMap;

public class Storage {

    // Seizoen in soort van periode klasse

    private HashMap<Product,Integer> productStorage;
    private HashMap<Product,Integer> minProductStorage;

    public Storage() {
        this.productStorage = new HashMap<>();
        this.minProductStorage = new HashMap<>();
    }

    public HashMap<Product, Integer> getProductStorage() {
        return productStorage;
    }

    public void setProductStorage(HashMap<Product, Integer> productStorage) {
        this.productStorage = productStorage;
    }

    public HashMap<Product, Integer> getMinProductStorage() {
        return minProductStorage;
    }

    public void setMinProductStorage(HashMap<Product, Integer> minProductStorage) {
        this.minProductStorage = minProductStorage;
    }

    public void addProductToStorage(Product product){
        if(!productStorage.containsKey(product)){
            productStorage.put(product, 0);
        }else{
            System.out.println("Dit product bestaat al in het assortiment");
        }
    }
    public void addProductToStorage(Product product, int amount){
        if(!productStorage.containsKey(product)){
            productStorage.put(product, 0);
            setMinAmountInStorage(product, amount);
        }else{
            System.out.println("Dit product bestaat al in het assortiment");
        }
    }

    public void setMinAmountInStorage(Product product, int amount){
        if(minProductStorage.containsKey(product)){
            minProductStorage.replace(product, amount);
        }else{
            minProductStorage.put(product,amount);
        }
    }
}
