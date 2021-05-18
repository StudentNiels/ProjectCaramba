package com.caramba.ordertool;

import java.util.ArrayList;
import java.util.List;

public class SupplierList {
    private final List<Supplier> suppliers = new ArrayList<>();

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

//#region delegate functions
    public Supplier get(int index){
        try{
            return suppliers.get(index);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    public void remove(int index){
        suppliers.remove(index);
    }

    public void clear(){
        suppliers.clear();
    }

    public void add(Supplier supplier){
        suppliers.add(supplier);
    }

    public int size(){
        return suppliers.size();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean contains(Object o) {
        return suppliers.contains(o);
    }

    //#endregion
    /**
     *
     * @param product product to search for
     * @return a arraylist of vendors that offer the given product
     */
    public SupplierList getSuppliersSellingProduct(Product product){
        SupplierList result = new SupplierList();
        for(Supplier v: suppliers){
            if(v.getProducts().contains(product)){
                result.add(v);
            }
        }
        return result;
    }
}
