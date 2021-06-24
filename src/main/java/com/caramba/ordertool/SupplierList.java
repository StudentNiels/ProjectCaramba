package com.caramba.ordertool;

import java.util.*;

public class SupplierList {
    private final HashMap<String, Supplier> suppliers = new HashMap<>();

    public HashMap<String, Supplier> getSuppliers() {
        return suppliers;
    }

    public void add(String id, Supplier supplier){
        suppliers.put(id, supplier);
    }

    public void add(Supplier supplier){
        //add with auto generated id
        String id = null;
        while(id == null || containsKey(id)){
            //reroll key if there is a collision
            id = UUID.randomUUID().toString();
        }
        add(id, supplier);
    }

    public boolean containsKey(String k) {
        return suppliers.containsKey(k);
    }

    //#endregion
    /**
     *
     * @param product product to search for
     * @return a arraylist of vendors that offer the given product
     */
    public SupplierList getSuppliersSellingProduct(Product product){
        SupplierList result = new SupplierList();
        for (Map.Entry<String, Supplier> entry : suppliers.entrySet()) {
            Supplier s = entry.getValue();
            if(s.getProducts().contains(product)){
                result.add(s);
            }
        }
        return result;
    }

    public Supplier getSupplierWithLowestAvgShippingTime(){
        Supplier result = null;
        for (Supplier supplier : suppliers.values()) {
            if(result == null || supplier.getAvgDeliveryTime() < result.getAvgDeliveryTime()){
                result = supplier;
            }
        }
        return result;
    }
}
