package com.caramba.vendors;

import java.util.ArrayList;
import java.util.List;

public class VendorList {
    private List<Vendor> vendors = new ArrayList<>();

    public List<Vendor> getVendors() {
        return vendors;
    }

    public Vendor getVendor(int index){
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

    public void addNew(String name, int deliveryTime){
        vendors.add(new Vendor(name, deliveryTime));
    }

    public int count(){
        return vendors.size();
    }
}
