package com.caramba.ordertool;

import java.util.Arrays;

//represents a typical year of sales of a certain product
public class MedianYear {
    private final int[] saleQuantities;
    private float[] percentages;

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
    public float getPercentageByMonthNumber(int monthNumber) {
        if (monthNumber < 1 || monthNumber > 12) {
            throw new IllegalArgumentException();
        }
        if(percentages == null){
            percentages = calculatePercentages(saleQuantities);
        }
        return percentages[monthNumber - 1];
    }

    /**
     * Calculates the distribution of values in percentages.
     * EXAMPLE
     * int[3, 5, 4] would return  int[36(%), 60(%), 48(%)]
     *
     * @param array array to calculate of
     * @return array with percentages
     */
    private float[] calculatePercentages(int[] array) {
        float[] result = new float[array.length];
        int total = Arrays.stream(array).sum();
        for (int i = 0; i < array.length; i++) {
            result[i] = (float) array[i] / total;
        }
        return result;
    }
}
