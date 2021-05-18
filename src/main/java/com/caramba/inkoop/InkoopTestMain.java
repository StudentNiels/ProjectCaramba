package com.caramba.inkoop;

public class InkoopTestMain {
    public static void main(String[] args){
        Supplier supplier1, supplier2, supplier3;

        supplier1 = new Supplier("Bremen");
        supplier2 = new Supplier("Leeuwarden");
        supplier3 = new Supplier("Groningen");

        supplier1.addProduct(new Product("asd123","Anti-insecten Autowasmiddel", Season.SUMMER),5);
        supplier2.addProduct(new Product("asd124","Anti-insecten Autowasmiddel", Season.SUMMER),14);
        supplier3.addProduct(new Product("asd122","Anti-insecten Autowasmiddel", Season.SUMMER),21);

        supplier1.addProduct(new Product("asd523","Autowasmiddel Glans"),8);
        supplier2.addProduct(new Product("asd524","Autowasmiddel Glans"),7);
        supplier3.addProduct(new Product("asd522","Autowasmiddel Glans"),18);

        supplier1.addProduct(new Product("asd823","Anti-vries Autowasmiddel", Season.WINTER),3);
        supplier2.addProduct(new Product("asd824","Anti-vries Autowasmiddel", Season.WINTER),28);
        supplier3.addProduct(new Product("asd822","Anti-vries Autowasmiddel", Season.WINTER),9);

        System.out.println("Supplier1 products");
        supplier1.listProducts();

        System.out.println("Supplier2 products");
        supplier2.listProducts();

        System.out.println("Supplier3 products");
        supplier3.listProducts();


    }
}
