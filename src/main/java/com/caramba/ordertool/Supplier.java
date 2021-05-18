package com.caramba.ordertool;




import java.security.InvalidParameterException;

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
//endregion

    /**
     * @param index of product to get
     * @return the product or null if the index is invalid
     */
    public Product getProduct(int index){
        try{
            return products.get(index);
        }catch(IndexOutOfBoundsException e){
            return null;
        }
    }

    public void addProduct(Product product){
        products.add(product);
    }
}
