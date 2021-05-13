package com.caramba.vendors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class VendorManagerTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream outputStreamOriginal = System.out;


    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(outputStreamOriginal);
    }

    //tests the clear, display, add and remove commandline arguments of main
    @Test
    void main() {
        String n = "TestVendor";
        String d = "7";
        //clear the vendors list
        VendorManager.main(new String[]{"clear"});
        //display. Console output should not contain the test vendor
        outputStream.reset();
        VendorManager.main(new String[]{});
        assertFalse(outputStream.toString().contains(n));
        assertFalse(outputStream.toString().contains(d));
        //add
        VendorManager.main(new String[]{"add", n , d});
        //display again. Console output should now contain the test vendor
        outputStream.reset();
        VendorManager.main(new String[]{});
        assertTrue(outputStream.toString().contains(n));
        assertTrue(outputStream.toString().contains(d));
        //remove
        VendorManager.main(new String[]{"rm", "0"});
        //display last time. Test vendor should be gone
        outputStream.reset();
        VendorManager.main(new String[]{});
        assertFalse(outputStream.toString().contains(n));
        assertFalse(outputStream.toString().contains(d));
    }
}