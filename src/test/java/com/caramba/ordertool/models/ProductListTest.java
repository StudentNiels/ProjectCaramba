package com.caramba.ordertool.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductListTest {

    @Test
    void getIDbyProduct() {
        ProductList productList = new ProductList();
        //add some products
        for(int i = 1; i <= 100; i++){
            String productID = "product #" + i;
            productList.add(productID, new Product("ProductNumber", productID));
        }
        for(int i = 1; i <= 100; i++){
            String productID = "product #" + i;
            Product product = productList.get(productID);
            assertEquals(productID, productList.getIDbyProduct(product));
        }
    }

    @Test
    void getIDbyProductInvalid() {
        ProductList productList = new ProductList();
        String productID = "productID";
        Product product = new Product("test", "test");
        assertNull(productList.getIDbyProduct(product));
        productList.add(productID, product);
        assertEquals(productID, productList.getIDbyProduct(product));
    }
}