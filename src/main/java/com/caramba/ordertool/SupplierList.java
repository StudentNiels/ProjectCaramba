package com.caramba.ordertool;

import java.util.*;

public class SupplierList {
    private final HashMap<UUID, Supplier> suppliers = new HashMap<>();

    public HashMap<UUID, Supplier> getSuppliers() {
        return suppliers;
    }

//#region delegate functions
    public Supplier get(UUID id){
        return suppliers.get(id);
    }

    public void remove(UUID id){
        suppliers.remove(id);
    }

    public void clear(){
        suppliers.clear();
    }

    public void add(UUID id, Supplier supplier){
        suppliers.put(id, supplier);
    }

    public void add(Supplier supplier){
        //add with auto generated id
        UUID id = null;
        while(id == null || containsKey(id)){
            //reroll key if there is a collision
            id = UUID.randomUUID();
        }
        add(id, supplier);
    }

    public int size(){
        return suppliers.size();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean contains(Supplier o) {
        return suppliers.containsValue(o);
    }

    public boolean containsKey(UUID k) {
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
        for (Map.Entry<UUID, Supplier> entry : suppliers.entrySet()) {
            Supplier s = entry.getValue();
            if(s.getProducts().contains(product)){
                result.add(s);
            }
        }
        return result;
    }
}
