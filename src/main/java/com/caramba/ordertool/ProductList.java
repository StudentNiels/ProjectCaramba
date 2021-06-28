package com.caramba.ordertool;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProductList {
    private final HashMap<String, Product> products = new HashMap<>();

    public HashMap<String, Product> getProducts() {
        return products;
    }

    //#region delegate functions
    public int size() {
        return products.size();
    }

    public Product get(String id) {
        return products.get(id);
    }

    public void add(String id, Product product) {
        products.put(id, product);
    }

    public void remove(String id) {
        products.remove(id);
    }

    public void clear() {
        products.clear();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean contains(Product p) {
        return products.containsValue(p);
    }

    public boolean containsKey(String id) {
        return products.containsKey(id);
    }
    //#endregion

    public String getIDbyProduct(Product product){
        for (Map.Entry<String, Product> entry : products.entrySet()) {
           Product p  = entry.getValue();
           if(product.equals(p)){
               return entry.getKey();
           }
        }
        return null;
    }

    public void add(Product product) {
        //add with auto generated id
        String id = null;
        while(id == null || containsKey(id)){
            //reroll key if there is a collision
            id = UUID.randomUUID().toString();
        }
        add(id, product);
    }
}
