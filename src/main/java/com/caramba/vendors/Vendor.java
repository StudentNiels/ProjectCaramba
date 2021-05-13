package com.caramba.vendors;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Vendor {
    private String name;
    /**
     * Estimated delivery time in days
     */
    private int DeliveryTime;

    public Vendor(
            @JsonProperty("name") String name,
            @JsonProperty("DeliveryTime") int DeliveryTime){
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
