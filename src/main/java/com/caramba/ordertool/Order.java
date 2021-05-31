package com.caramba.ordertool;

import java.util.HashMap;
import java.util.Map;

public class Order {

    private String order_date;
    private String order_nr;
    private HashMap<Product, Integer> shoppingCart;
    // TODO: Leverancier opvangen voor de inkopen

    public Order(String order_nr, HashMap<String, Product> shoppingCart){
        FireStoreConfig fireStoreConfig = new FireStoreConfig();
        fireStoreConfig.dbConnect();
        fireStoreConfig.setupOrderDocument(this.order_nr = order_nr, this.order_date = fireStoreConfig.getTimeStamp());
    }

    public Order(String order_date, String order_nr, HashMap<Product, Integer> shoppingCart) {
        this.order_date = order_date;
        this.order_nr = order_nr;
        this.shoppingCart = shoppingCart;
    }

    //region Getters and Setters
    public String getOrderDate() {
        return order_date;
    }

    public void setOrderDate(String orderDate) {
        this.order_date = orderDate;
    }


    public HashMap<Product, Integer> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(HashMap<Product, Integer> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
    //endregion

    public void addToShoppingCart(Product product, int amount){
        if(shoppingCart.containsKey(product)){
            shoppingCart.replace(product,(shoppingCart.get(product) + amount));
        }
        else{
            shoppingCart.put(product, amount);
        }
    }

    public void listShoppingCart(){
        System.out.println("Orderlist items:");
        for(Map.Entry<Product, Integer> product : this.shoppingCart.entrySet()){
            Product item = product.getKey();
            int amount = product.getValue();

            System.out.println(item.getProduct_num() + " " + item.getProduct_descript() + "/" + amount);
        }
    }
}