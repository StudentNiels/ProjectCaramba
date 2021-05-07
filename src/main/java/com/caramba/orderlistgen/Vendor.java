package com.caramba.orderlistgen;


public class Vendor {
    private String name;
    private int DeliveryTime;

    public Vendor(String name, int DeliveryTime){
        this.DeliveryTime = DeliveryTime;
        this.name = name;
    }
//region Getters and Setters
    public int getDeliveryTime() {
        return DeliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        DeliveryTime = deliveryTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//endregion
}
