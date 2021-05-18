package com.caramba.ordertool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierListTest {

    SupplierList vendors = new SupplierList();
    ProductList products = new ProductList();

    @BeforeEach
    void setUp() {
        vendors.add(new Supplier("Vendor1", 10));
        vendors.add(new Supplier("CoolVendor2", 10));
        vendors.add(new Supplier("VendorNumber3", 10));
        products.add(new Product("abc", "testPoduct"));
        products.add(new Product("123456789", "testme"));
        //the first vendor has one product
        vendors.get(0).addProduct(products.get(0));
        //the second vendor has both products
        vendors.get(1).addProduct(products.get(0));
        vendors.get(1).addProduct(products.get(1));
        //the third vendor has no products
    }


    @Test
    void getVendorsSellingProduct() {
        //first product
        SupplierList Result = vendors.getSuppliersSellingProduct(products.get(0));

        assertTrue(Result.contains(vendors.get(0)));
        assertTrue(Result.contains(vendors.get(1)));
        assertFalse(Result.contains(vendors.get(2)));
        //second product
        Result = vendors.getSuppliersSellingProduct(products.get(1));
        assertFalse(Result.contains(vendors.get(0)));
        assertTrue(Result.contains(vendors.get(1)));
        assertFalse(Result.contains(vendors.get(2)));
    }
}