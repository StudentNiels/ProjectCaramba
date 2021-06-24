package com.caramba.ordertool;

import java.util.HashMap;
import java.util.Map;

public class ProductList {
    private final HashMap<String, Product> products = new HashMap<>();

    public HashMap<String, Product> getProducts() {
        return products;
    }

    public Product get(String id) {
        return products.get(id);
    }

    public void add(String id, Product product) {
        products.put(id, product);
    }

    public boolean contains(Product p) {
        return products.containsValue(p);
    }

    public boolean containsKey(String id) {
        return products.containsKey(id);
    }
    //#endregion

    public String getIDbyProduct(Product product) {
        for (Map.Entry<String, Product> entry : products.entrySet()) {
            Product p = entry.getValue();
            if (product.equals(p)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
