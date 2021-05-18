package com.caramba.ordertool;

import java.util.ArrayList;
import java.util.List;

public class VendorList{
    private final List<Vendor> vendors = new ArrayList<>();

    public List<Vendor> getVendors() {
        return vendors;
    }

//#region delegate functions
    public Vendor get(int index){
        try{
            return vendors.get(index);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    public void remove(int index){
        vendors.remove(index);
    }

    public void clear(){
        vendors.clear();
    }

    public void add(Vendor vendor){
        vendors.add(vendor);
    }

    public int size(){
        return vendors.size();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean contains(Object o) {
        return vendors.contains(o);
    }

    //#endregion
    /**
     *
     * @param product product to search for
     * @return a arraylist of vendors that offer the given product
     */
    public VendorList getVendorsSellingProduct(Product product){
        VendorList result = new VendorList();
        for(Vendor v: vendors){
            if(v.getProducts().contains(product)){
                result.add(v);
            }
        }
        return result;
    }
}
