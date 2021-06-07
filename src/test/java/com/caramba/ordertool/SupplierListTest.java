package com.caramba.ordertool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SupplierListTest {

    SupplierList suppliers = new SupplierList();
    ProductList products = new ProductList();

    String supplierID1 = UUID.randomUUID().toString();
    String supplierID2 = UUID.randomUUID().toString();
    String supplierID3 = UUID.randomUUID().toString();
    String productID1 = UUID.randomUUID().toString();
    String productID2 = UUID.randomUUID().toString();
    @BeforeEach
    void setUp() {

        suppliers.add(supplierID1, new Supplier("Supplier1", 10));
        suppliers.add(supplierID2, new Supplier("CoolSupplier2", 10));
        suppliers.add(supplierID3, new Supplier("SupplierNumber3", 10));
        products.add(productID1, new Product("abc", "testPoduct"));
        products.add(productID2, new Product("123456789", "testme"));
        //the first supplier has one product
        suppliers.get(supplierID1).addProduct(products.get(productID1));
        //the second supplier has both products
        suppliers.get(supplierID2).addProduct(products.get(productID1));
        suppliers.get(supplierID2).addProduct(products.get(productID2));
        //the third supplier has no products
    }


    @Test
    void getSuppliersSellingProduct() {
        //first product
        SupplierList Result = suppliers.getSuppliersSellingProduct(products.get(productID1));
        assertTrue(Result.contains(suppliers.get(supplierID1)));
        assertTrue(Result.contains(suppliers.get(supplierID2)));
        assertFalse(Result.contains(suppliers.get(supplierID3)));
        //second product
        Result = suppliers.getSuppliersSellingProduct(products.get(productID2));
        assertFalse(Result.contains(suppliers.get(supplierID1)));
        assertTrue(Result.contains(suppliers.get(supplierID2)));
        assertFalse(Result.contains(suppliers.get(supplierID3)));
    }
}