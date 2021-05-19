package com.caramba.inkoop;

import org.apache.commons.collections4.KeyValue;

import java.util.HashMap;
import java.util.Map;

public class Supplier {

    private String name;
    private HashMap<Product, Integer> products;

    public Supplier(String name) {
        this.name = name;
        this.products = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Product,Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Product,Integer> products) {
        this.products = products;
    }

    /**
     * @param product Het product die toegevoegd moet worden
     * @param leverTijd  Levertijd in dagen*/
    public void addProduct(Product product, int leverTijd){
        this.products.put(product, leverTijd);
    }

    public void listProducts(){
        for(Map.Entry<Product, Integer> product : products.entrySet()){
            Product selectedProduct = product.getKey();
            int leverTijd = product.getValue();

            System.out.println(selectedProduct.getArticlenr() + " " + selectedProduct.getDescription() + " Levertijd: " + leverTijd + " dagen");
        }
    }

    public Product getProduct(String articleNr){
        Product selectedProduct = null;
        for(Map.Entry<Product,Integer> product : this.products.entrySet()){
            if(product.getKey().getArticlenr().equals(articleNr)){
                selectedProduct = product.getKey();
            }
        }
        return selectedProduct;
    }
}
