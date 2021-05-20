package com.caramba.ordertool;




import java.security.InvalidParameterException;
import java.util.Map;
import java.util.UUID;

public class Supplier {
    private String name;
    /**
     * Estimated delivery time in days
     */
    private int DeliveryTime;

    /**
     * Products sold by this supplier
     */
    private final ProductList products = new ProductList();

    public Supplier(String name, int DeliveryTime){
        setDeliveryTime(DeliveryTime);
        this.name = name;
    }

    public Supplier(String name){
        this(name, 0);
    }
//region Getters and Setters
    public int getDeliveryTime() {
        return DeliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        if(deliveryTime < 0){
            throw new InvalidParameterException();
        }
        DeliveryTime = deliveryTime;
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

    public boolean containsProductWithKey(UUID k){
        return products.containsKey(k);
    }
//endregion

    /**
     * @param id of product to get
     * @return the product or null if the index is invalid
     */
    public Product getProduct(UUID id){
        return products.get(id);
    }

    public void addProduct(UUID id, Product product){
        products.add(id, product);
    }

    public void addProduct(Product product){
        UUID id = null;
        while(id == null || containsProductWithKey(id)){
            //reroll key if there is a collision
            id = UUID.randomUUID();
        }
        products.add(id, product);
    }

    public void listProducts(){
        for(Map.Entry<UUID, Product> product : products.getProducts().entrySet()){
            UUID ID = product.getKey();
            Product selectedProduct = product.getValue();

            System.out.println("ProductID = " + ID + "/" + selectedProduct.getProductNum() + " " + selectedProduct.getDescription());
        }
    }
}