package com.caramba.ordertool;

import java.util.ArrayList;
import java.util.List;

public class VendorList {
    private final List<Vendor> vendors = new ArrayList<>();

    public List<Vendor> getVendors() {
        return vendors;
    }

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
}
