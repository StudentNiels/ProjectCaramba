package com.caramba.orderlistgen;

import java.util.ArrayList;
import java.util.List;

public class VendorList {
    List<Vendor> vendors = new ArrayList<>();

    public List<Vendor> getVendors() {
        return vendors;
    }

    public Vendor getVendor(int index){
        return vendors.get(index);
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
}
