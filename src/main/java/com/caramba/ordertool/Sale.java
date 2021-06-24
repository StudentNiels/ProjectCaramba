package com.caramba.ordertool;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Sale {

    private final LocalDateTime date;
    private final HashMap<String, Integer> products;

    public Sale(LocalDateTime date) {
        this.date = date;
        this.products = new HashMap<>();
    }

    public Sale(HashMap<String, Integer> products, LocalDateTime date) {
        this.date = date;
        this.products = products;
    }

    //region Getters and Setters
    public int getAmountByID(String uuid) throws InvalidParameterException {
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            if (entry.getKey().equals(uuid)) {
                return entry.getValue();
            }
        }
        throw new InvalidParameterException("This is not a valid id");
    }

    public LocalDateTime getDate() {
        return date;
    }

    public HashMap<String, Integer> getProducts() {
        return products;
    }

    //endregion
    public void addToProducts(String uuid, int amount) {
        if (products.containsKey(uuid)) {
            products.replace(uuid, (products.get(uuid) + amount));
        } else {
            products.put(uuid, amount);
        }
    }
}