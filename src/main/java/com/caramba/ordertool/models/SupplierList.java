package com.caramba.ordertool.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * a collection of suppliers
 */
public class SupplierList {
    private final HashMap<String, Supplier> suppliers = new HashMap<>();

    /**
     * Returns a hashmap containing supplier id's as keys and the supplier as value.
     *
     * @return a hashmap containing suppliers
     */
    public HashMap<String, Supplier> getSuppliers() {
        return suppliers;
    }

    /**
     * Add a supplier to the SupplierList using the specified key.
     *
     * @param id       key to map the supplier to
     * @param supplier supplier to add
     */
    public void add(String id, Supplier supplier) {
        suppliers.put(id, supplier);
    }

    /**
     * Add a supplier to the supplierList using a randomized key.
     *
     * @param supplier supplier to add
     */
    public void add(Supplier supplier) {
        //add with auto generated id
        String id = null;
        while (id == null || containsKey(id)) {
            //roll again if there is a collision
            id = UUID.randomUUID().toString();
        }
        add(id, supplier);
    }

    /**
     * Returns true if the supplierList contains the specified key
     *
     * @param key key to test for
     * @return true if the supplierList contains the specified key
     */
    public boolean containsKey(String key) {
        return suppliers.containsKey(key);
    }

    //#endregion

    /**
     * Returns the suppliers that sell a specified product
     *
     * @param product product to search for
     * @return a arraylist of vendors that offer the specified product
     */
    public SupplierList getSuppliersSellingProduct(Product product) {
        SupplierList result = new SupplierList();
        for (Map.Entry<String, Supplier> entry : suppliers.entrySet()) {
            Supplier s = entry.getValue();
            if (s.getProducts().contains(product)) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * Returns the supplier that has the lowest average shipping time of this SupplierList
     *
     * @return the supplier that has the lowest average shipping time
     */
    public Supplier getSupplierWithLowestAvgShippingTime() {
        Supplier result = null;
        for (Supplier supplier : suppliers.values()) {
            if (result == null || supplier.getAvgDeliveryTime() < result.getAvgDeliveryTime()) {
                result = supplier;
            }
        }
        return result;
    }
}
