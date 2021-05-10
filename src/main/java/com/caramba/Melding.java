package com.caramba;

import java.util.HashMap;
import java.util.Map;

public class Melding {
    private HashMap<String, Integer> products;
    private HashMap<String, Integer> minimumStock;
    private HashMap<String, Integer> soldProducts;
    //TODO: Create a map of products that need to be bought by the company

    public Melding(){

        products = new HashMap<>();
        minimumStock = new HashMap<>();
        soldProducts = new HashMap<>();
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
            int stockStock = itemStock.getValue();

            for (Map.Entry<String,Integer> item : products.entrySet()) {
                String product = item.getKey();
                int stock = item.getValue();
                int lowStock = stockStock / 100 * 20;

                //als Verkochte items - voorraad X percentage raakt, geef melding
                if(product.equals(productStock) && stock <= lowStock){
                    if(stock == 0){
                        System.out.println("Er is momenteel geen voorraad meer van het product '" + product + "' in het warenhuis.");
                    }else{
                        System.out.println("Er is nog een lage aantal van product '" + product + "' in het warenhuis. Er zijn momenteel nog " + stock + " in voorraad.");
                    }
                }
            }
        }

    }

    // Main method used for testing
    // TODO: make unit test for Melding.java
    public static void main(String[] args){

        Melding melding = new Melding();

        System.out.println("---------------------------------------------------");
        System.out.println("Start product listing");
        melding.listProducts();
        System.out.println("End product listing");
        System.out.println("---------------------------------------------------");

        melding.sellProduct("Item-1",10);
        melding.sellProduct("Item-5",50);
        melding.sellProduct("Item-8",80);
        melding.sellProduct("Item-10",100);

        System.out.println("---------------------------------------------------");
        System.out.println("Start product listing after order");
        melding.listProducts();
        System.out.println("End product listing after order");
        System.out.println("---------------------------------------------------");

        System.out.println("---------------------------------------------------");
        System.out.println("Start testing notification on low stock");
        melding.notifyLowStock();
        System.out.println("Raising Item-5 minimumStock");
        melding.setMinimumStock("Item-5", 500);
        melding.notifyLowStock();
        System.out.println("End testing notification on low stock");
        System.out.println("---------------------------------------------------");

        System.out.println("---------------------------------------------------");
        System.out.println("Start checking Sold items");
        melding.listSoldProducts();
        System.out.println("End checking Sold items");
        System.out.println("---------------------------------------------------");


    }

}
