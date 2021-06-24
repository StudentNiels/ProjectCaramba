package com.caramba.ordertool.models;

//represents a typical year of sales of a certain product
public class MedianYear {
    private final int[] saleQuantities;

    public MedianYear(int[] saleQuantities) {
        if (saleQuantities.length != 12) {
            throw new IllegalArgumentException();
        }
        this.saleQuantities = saleQuantities;
    }

    public int getByMonthNumber(int monthNumber) {
        if (monthNumber < 1 || monthNumber > 12) {
            throw new IllegalArgumentException();
        }
        return saleQuantities[monthNumber - 1];
    }

}
