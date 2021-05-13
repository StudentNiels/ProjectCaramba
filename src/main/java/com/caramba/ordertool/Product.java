package com.caramba.ordertool;

public class Product {
    private String id;
    private String description;

    public Product(String id, String description) {
        this.id = id;
        this.description = description;
    }

    //region Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    //endregion
}
