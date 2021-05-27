package com.caramba.ordertool;

public class InkoopTestMain {
    public static void main(String[] args){
        Orderlist orderlist = new Orderlist();
        Order order1 = new Order();

        Product product1 = new Product("asd123","Anti-insecten Autowasmiddel", 100);
        Product product2 = new Product("asd124","Anti-insecten Autowasmiddel", 100);
        Product product3 = new Product("asd122","Anti-insecten Autowasmiddel", 100);
        Product product4 = new Product("asd523","Autowasmiddel Glans", 100);
        Product product5 = new Product("asd524","Autowasmiddel Glans", 100);
        Product product6 = new Product("asd522","Autowasmiddel Glans", 100);
        Product product7 = new Product("asd823","Anti-vries Autowasmiddel", 100);
        Product product8 = new Product("asd824","Anti-vries Autowasmiddel", 100);
        Product product9 = new Product("asd822","Anti-vries Autowasmiddel", 100);

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
