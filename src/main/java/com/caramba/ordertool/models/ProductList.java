package com.caramba.ordertool.models;

import java.util.HashMap;
import java.util.Map;

/**
 * A collection of products
 */
public class ProductList {
    private final HashMap<String, Product> products = new HashMap<>();

    public HashMap<String, Product> getProducts() {
        return products;
    }

    /**
     * Returns the product that is mapped to the specified key string or null if this ProductList does not contain it.
     *
     * @param id the key to look up
     * @return the product mapped to the specified key or null if it doesn't exist.
     */
    public Product get(String id) {
        return products.get(id);
    }

    /**
     * Adds a product to the ProductList with the specified key
     *
     * @param id      the key to map this product to
     * @param product the product to add to this ProductList
     */
    public void add(String id, Product product) {
        products.put(id, product);
    }

    /**
     * Returns true if this ProductList contains the specified product.
     *
     * @param product product whose presence in this ProductList is to be tested
     * @return True if this ProductList contains the specified product
     */
    public boolean contains(Product product) {
        return products.containsValue(product);
    }

    /**
     * Returns true if this productList contains the specified key string.
     *
     * @param id key string whose presence in this ProductList is to be tested
     * @return true if this productList contains the specified key string
     */
    public boolean containsKey(String id) {
        return products.containsKey(id);
    }

    /**
     * Returns the key string of the specified product. This will be inconsistent if there are multiple keys mapped to the same product!
     *
     * @param product the product to get the key for
     * @return the key string of the specified product, or null if the product does not exist in this ProductList
     */
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
