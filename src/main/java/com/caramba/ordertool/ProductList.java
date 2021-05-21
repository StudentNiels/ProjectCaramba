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

    public void showProductBySeason(String season){
        ArrayList<Product> returnedProducts = new ArrayList<>();
        switch (season){
            case "zomer":
                for (Map.Entry<UUID, Product> productEntry : products.entrySet()) {
                    if(productEntry.getValue().getSeason().equals(Season.SUMMER))
                    returnedProducts.add(productEntry.getValue());
                }
                break;
            case "herfst":
                for (Map.Entry<UUID, Product> productEntry : products.entrySet()) {
                    if(productEntry.getValue().getSeason().equals(Season.FALL))
                        returnedProducts.add(productEntry.getValue());
                }
                break;
            case "winter":
                for (Map.Entry<UUID, Product> productEntry : products.entrySet()) {
                    if(productEntry.getValue().getSeason().equals(Season.WINTER))
                        returnedProducts.add(productEntry.getValue());
                }
                break;
            case "lente":
                for (Map.Entry<UUID, Product> productEntry : products.entrySet()) {
                    if(productEntry.getValue().getSeason().equals(Season.SPRING))
                        returnedProducts.add(productEntry.getValue());
                }
                break;
            case "allround":
                for (Map.Entry<UUID, Product> productEntry : products.entrySet()) {
                    if(productEntry.getValue().getSeason().equals(Season.ALLROUND))
                        returnedProducts.add(productEntry.getValue());
                }
                break;
        }
        if(returnedProducts.size() == 0){
            System.out.println("No items have been found");
        }else{
            for (int i = 0; i < returnedProducts.size(); i++) {
                System.out.println(returnedProducts.get(i).getProductNum() + "/" + returnedProducts.get(i).getDescription());
            }
        }
    }

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
