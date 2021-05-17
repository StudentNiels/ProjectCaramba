package com.caramba.ordertool;

import java.util.ArrayList;

public class ProductList {
    private ArrayList<Product> products = new ArrayList<>();

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

    public boolean contains(Object o) {
        return products.contains(o);
    }
}
