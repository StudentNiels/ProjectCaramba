package com.caramba.vendors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VendorListTest {
    VendorList vl = new VendorList();

    @Test
    void getVendor() {
        String s = "test";
        assertNull(vl.getVendor(0));
        vl.addNew(s, 7);
        assertEquals(vl.getVendor(0).getName(), s);
    }

}