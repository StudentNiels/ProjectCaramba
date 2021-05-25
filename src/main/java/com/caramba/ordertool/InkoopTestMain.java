package com.caramba.ordertool;

public class InkoopTestMain {
    public static void main(String[] args){
        Supplier supplier1, supplier2, supplier3;
        Orderlist orderlist = new Orderlist();
        Order order1 = new Order();

        supplier1 = new Supplier("Bremen");
        supplier2 = new Supplier("Leeuwarden");
        supplier3 = new Supplier("Groningen");

        Product product1 = new Product("asd123","Anti-insecten Autowasmiddel", "zomer");
        Product product2 = new Product("asd124","Anti-insecten Autowasmiddel", "zomer");
        Product product3 = new Product("asd122","Anti-insecten Autowasmiddel", "zomer");
        Product product4 = new Product("asd523","Autowasmiddel Glans");
        Product product5 = new Product("asd524","Autowasmiddel Glans");
        Product product6 = new Product("asd522","Autowasmiddel Glans");
        Product product7 = new Product("asd823","Anti-vries Autowasmiddel", "winter");
        Product product8 = new Product("asd824","Anti-vries Autowasmiddel", "winter");
        Product product9 = new Product("asd822","Anti-vries Autowasmiddel", "winter");

        // Supplier test, commented out in order to make adding order works
        /*
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
        */

        order1.addToShoppingCart(product1,50);
        order1.addToShoppingCart(product2,75);
        order1.addToShoppingCart(product3,125);
        order1.addToShoppingCart(product4,555);
        order1.addToShoppingCart(product5,123);
        order1.addToShoppingCart(product6,20);
        order1.addToShoppingCart(product7,5);
        order1.addToShoppingCart(product8,30);
        order1.addToShoppingCart(product9,99);

        orderlist.addToOrderList(order1);

        orderlist.listOrders();
    }
}
