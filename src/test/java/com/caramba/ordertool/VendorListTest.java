package com.caramba.ordertool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VendorListTest {
    VendorList vl = new VendorList();

    //Assure index out of bounds exception is getting caught in the function
    @Test
    void getVendor() {
        String s = "test";
        assertNull(vl.getVendor(0));
        vl.addNew(s, 7);
        assertEquals(vl.getVendor(0).getName(), s);
    }

}