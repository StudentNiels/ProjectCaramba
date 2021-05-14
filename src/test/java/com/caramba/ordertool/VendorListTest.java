package com.caramba.ordertool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VendorListTest {
    VendorList vl = new VendorList();

    //Assure index out of bounds exception is getting caught in the function
    @Test
    void getVendor() {
        String s = "test";
        assertNull(vl.get(0));
        vl.add(new Vendor(s, 7));
        assertEquals(vl.get(0).getName(), s);
    }

}