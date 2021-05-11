package com.caramba;

import java.util.HashMap;
import java.util.Map;

public class Notification {
    private HashMap<String, Integer> products;
    private HashMap<String, Integer> minimumStock;
    private HashMap<String, Integer> soldProducts;
    private HashMap<String, Integer> orderList;

    public Notification(){

        products = new HashMap<>();
        minimumStock = new HashMap<>();
        soldProducts = new HashMap<>();
        orderList = new HashMap<>();
        fillProductsMap();
    }

    /** Test method for filling the products and minimumStock maps */
    public void fillProductsMap(){
        for (int i = 1; i <= 10; i++) {
            products.put(("Item-" + i), 100);
            minimumStock.put(("Item-" + i), 100);
        }
    }

    /**  Make a list from all the products in products */
    public void listProducts(){
        for (Map.Entry<String,Integer> item : products.entrySet()) {
            String product = item.getKey();
            int stock = item.getValue();

            System.out.println("Product: " + product);
            System.out.println("In stock: " + stock);
        }
    }

    /**
     * This method allows the user to set the wished minimum amount to keep in stock
     * @param itemName This is currently a placeholder for the actual product object
     * @param amount The desired minimum amount
     */
    public void setMinimumStock(String itemName, int amount){
        minimumStock.replace(itemName, amount);
    }

    /**
     * This method will be used to sell a product and replace the amount available in products
     * @param itemName This is currently a placeholder for the actual product object
     * @param amount The amount sold
     * TODO: ensure that negative numbers will be added to additional demand list
     */
    public void sellProduct(String itemName, int amount){
        /*
        * Als (stock - amount) < 0
        * Zet stock naar 0
        * voeg amount toe aan soldProducts
        * voeg amount toe aan orderList
        *
        *
        *
        * */
        products.replace(itemName, products.get(itemName) - amount);
        if(soldProducts.containsKey(itemName)){
            soldProducts.replace(itemName, soldProducts.get(itemName) + amount);
        }else{
            soldProducts.put(itemName, amount);
        }
    }

    /** This method is used to list the sold products */
    public void listSoldProducts(){
        for (Map.Entry<String,Integer> item : soldProducts.entrySet()) {
            String product = item.getKey();
            int stock = item.getValue();

            System.out.println(product+"/"+stock);
        }
    }

    /** Loop through the items listed in orderList */
    public void showOrderList(){
        System.out.println("Items to be ordered:");
        for(Map.Entry<String, Integer> item : orderList.entrySet()){
            String product = item.getKey();
            int toBeOrdered = item.getValue();

            System.out.println(product+"/"+toBeOrdered);
        }
    }

    /** Add item to orderList
     * @param itemName Item name, will be replaced with Product object at some point
     * @param amount The amount that has to be ordered
     */
    public void addToOrderList(String itemName, int amount){
        if(orderList.containsKey(itemName)){
            orderList.replace(itemName, orderList.get(itemName) + amount);
        }else{
            orderList.put(itemName, amount);
        }
    }

    /**
     * This method will be used to keep track of the stocks and if something hits 20% or lower of the wanted minimum
     * it will give a notification of the products which meet this requirement
     *
     * Currently return nothing, but should a list of items to be ordered
     * TODO: Return a list of products that need to be bought
     * */
    public void notifyLowStock(){
        //TODO: think of solution to use sold Items to decide when to resupply
        for (Map.Entry<String,Integer> itemStock : minimumStock.entrySet()) {
            String productStock = itemStock.getKey();
            int minStock = itemStock.getValue();

            for (Map.Entry<String,Integer> item : products.entrySet()) {
                String product = item.getKey();
                int stock = item.getValue();
                int lowStock = minStock / 100 * 20;

                //als Verkochte items - voorraad X percentage raakt, geef melding
                if(product.equals(productStock) && stock <= lowStock){
                    addToOrderList(product,(minStock - stock));
                    if(stock == 0 || stock < 0){
                        System.out.println("Er is momenteel geen voorraad meer van het product '" + product + "' in het warenhuis.");
                    }else{
                        System.out.println("Er is nog een lage aantal van product '" + product + "' in het warenhuis. Er zijn momenteel nog " + stock + " in voorraad.");
                    }
                }
            }
        }
    }

    public HashMap<String, Integer> getProducts() {
        return products;
    }

    public HashMap<String, Integer> getMinimumStock() {
        return minimumStock;
    }

    public HashMap<String, Integer> getSoldProducts() {
        return soldProducts;
    }

    public HashMap<String, Integer> getOrderList() {
        return orderList;
    }

    // Main method used for testing
    public static void main(String[] args){

        Notification notification = new Notification();
/*
        System.out.println("---------------------------------------------------");
        System.out.println("Start product listing");
        melding.listProducts();
        System.out.println("End product listing");
        System.out.println("---------------------------------------------------");

        melding.sellProduct("Item-1",10);
        melding.sellProduct("Item-5",50);
        melding.sellProduct("Item-8",80);
        melding.sellProduct("Item-10",150);

        System.out.println("---------------------------------------------------");
        System.out.println("Start product listing after order");
        melding.listProducts();
        System.out.println("End product listing after order");
        System.out.println("---------------------------------------------------");
*/
        System.out.println("---------------------------------------------------");
        System.out.println("Start testing notification on low stock");
        notification.notifyLowStock();
        System.out.println("Raising Item-5 minimumStock");
        notification.setMinimumStock("Item-5", 500);
        notification.notifyLowStock();
        System.out.println("End testing notification on low stock");
        System.out.println("---------------------------------------------------");
/*
        System.out.println("---------------------------------------------------");
        System.out.println("Start checking Sold items");
        melding.listSoldProducts();
        System.out.println("End checking Sold items");
        System.out.println("---------------------------------------------------");

        System.out.println("---------------------------------------------------");
        System.out.println("Start checking orderList");
        melding.showOrderList();
        System.out.println("End checking orderList");
        System.out.println("---------------------------------------------------");
*/
    }

}
