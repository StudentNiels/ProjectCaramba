package com.caramba.ordertool;




import java.security.InvalidParameterException;
import java.util.Map;
import java.util.UUID;

public class Supplier {
    private String name;
    /**
     * Estimated delivery time in days
     */
    private Integer avgDeliveryTime;
    /**
     * Products sold by this supplier
     */
    private final ProductList products = new ProductList();


    public Supplier(String name, int avgDeliveryTime){
        this.name = name;
        this.avgDeliveryTime = avgDeliveryTime;
    }

    public Supplier(){

    }

    //region Getters and Setters
    public int getAvgDeliveryTime() {
        return avgDeliveryTime;
    }

    public void setAvgDeliveryTime(int avgDeliveryTime) {
        if(avgDeliveryTime < 0){
            throw new InvalidParameterException();
        }
        this.avgDeliveryTime = avgDeliveryTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductList getProducts() {
        return products;
    }

    public boolean containsProductWithKey(String k){
        return products.containsKey(k);
    }

    public boolean containsProduct(Product p){
        return products.contains(p);
    }
    //endregion

    /**
     * @param id of product to get
     * @return the product or null if the index is invalid
     */
    public Product getProduct(String id){
        return products.get(id);
    }

    public void addProduct(String id, Product product){
        products.add(id, product);
    }

    public void addProduct(Product product){
        String id = null;
        while(id == null || containsProductWithKey(id)){
            //reroll key if there is a collision
            id = UUID.randomUUID().toString();
        }
        products.add(id, product);
    }

    public void listProducts(){
        for(Map.Entry<String, Product> product : products.getProducts().entrySet()){
            String ID = product.getKey();
            Product selectedProduct = product.getValue();

            System.out.println("ProductID = " + ID + "/" + selectedProduct.getProductNum() + " " + selectedProduct.getDescription());
        }
    }
}