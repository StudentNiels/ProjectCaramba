package com.caramba.ordertool;

import java.util.ArrayList;

public class ProductList {
    private final ArrayList<Product> products = new ArrayList<>();

    //#region delegate functions
    public int size() {
        return products.size();
    }

    public Product get(int index) {
        return products.get(index);
    }

    public void add(Product product) {
        products.add(product);
    }

    public void remove(int index) {
        products.remove(index);
    }

    public void clear() {
        products.clear();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean contains(Object o) {
        return products.contains(o);
    }
    //#endregion
}
