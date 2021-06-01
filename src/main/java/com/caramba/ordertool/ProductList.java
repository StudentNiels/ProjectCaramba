package com.caramba.ordertool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProductList {
    private final HashMap<UUID, Product> products = new HashMap<>();

    public HashMap<UUID, Product> getProducts() {
        return products;
    }

    //#region delegate functions
    public int size() {
        return products.size();
    }

    public Product get(UUID id) {
        return products.get(id);
    }

    public void add(UUID id, Product product) {
        products.put(id, product);
    }


    public void remove(UUID id) {
        products.remove(id);
    }

    public void clear() {
        products.clear();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean contains(Product p) {
        return products.containsValue(p);
    }

    public boolean containsKey(UUID id) {
        return products.containsKey(id);
    }
    //#endregion

    public void add(Product product) {
        //add with auto generated id
        UUID id = null;
        while(id == null || containsKey(id)){
            //reroll key if there is a collision
            id = UUID.randomUUID();
        }
        add(id, product);
    }
}
