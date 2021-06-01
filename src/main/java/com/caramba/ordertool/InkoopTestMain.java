package com.caramba.ordertool;

public class InkoopTestMain {
    public static void main(String[] args){
        Saleslist saleslist = new Saleslist();
        Sale order1 = new Sale();
/*
        Product product1 = new Product("asd123","Anti-insecten Autowasmiddel", 100);
        Product product2 = new Product("asd124","Anti-insecten Autowasmiddel", 100);
        Product product3 = new Product("asd122","Anti-insecten Autowasmiddel", 100);
        Product product4 = new Product("asd523","Autowasmiddel Glans", 100);
        Product product5 = new Product("asd524","Autowasmiddel Glans", 100);
        Product product6 = new Product("asd522","Autowasmiddel Glans", 100);
        Product product7 = new Product("asd823","Anti-vries Autowasmiddel", 100);
        Product product8 = new Product("asd824","Anti-vries Autowasmiddel", 100);
        Product product9 = new Product("asd822","Anti-vries Autowasmiddel", 100);

        order1.addToProducts(product1,50);
        order1.addToProducts(product2,75);
        order1.addToProducts(product3,125);
        order1.addToProducts(product4,555);
        order1.addToProducts(product5,123);
        order1.addToProducts(product6,20);
        order1.addToProducts(product7,5);
        order1.addToProducts(product8,30);
        order1.addToProducts(product9,99);
*/
        saleslist.addToSalesList(order1);

        saleslist.listSales();
    }
}
