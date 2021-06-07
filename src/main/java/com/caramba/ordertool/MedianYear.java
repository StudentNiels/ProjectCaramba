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
//#region Per month getters
    public int getJanuary() {
        return getByMonthNumber(1);
    }

    public int getFebruary() {
        return getByMonthNumber(2);
    }

    public int getMarch() {
        return getByMonthNumber(3);
    }

    public int getApril() {
        return getByMonthNumber(4);
    }

    public int getMay() {
        return getByMonthNumber(5);
    }

    public int getJune() {
        return getByMonthNumber(6);
    }

    public int getJuly() {
        return getByMonthNumber(7);
    }

    public int getAugust() {
        return getByMonthNumber(8);
    }

    public int getSeptember() {
        return getByMonthNumber(9);
    }

    public int getOctober() {
        return getByMonthNumber(10);
    }

    public int getNovember() {
        return getByMonthNumber(11);
    }

    public int getDecember() {
        return getByMonthNumber(12);
    }
    //#endregion
    public int getByMonthNumber(int monthNumber) {
        if (monthNumber < 1 || monthNumber > 12) {
            throw new IllegalArgumentException();
        }
        return saleQuantities[monthNumber - 1];
    }
//#region Per month percentage getters
    public float getJanuaryPercentage() {
        return getPercentageByMonthNumber(1);
    }

    public Float getFebruaryPercentage() {
        return getPercentageByMonthNumber(2);
    }

    public Float getMarchPercentage() {
        return getPercentageByMonthNumber(3);
    }

    public Float getAprilPercentage() {
        return getPercentageByMonthNumber(4);
    }

    public Float getMayPercentage() {
        return getPercentageByMonthNumber(5);
    }

    public Float getJunePercentage() {
        return getPercentageByMonthNumber(6);
    }

    public Float getJulyPercentage() {
        return getPercentageByMonthNumber(7);
    }

    public Float getAugustPercentage() {
        return getPercentageByMonthNumber(8);
    }

    public Float getSeptemberPercentage() {
        return getPercentageByMonthNumber(9);
    }

    public Float getOctoberPercentage() {
        return getPercentageByMonthNumber(10);
    }

    public Float getNovemberPercentage() {
        return getPercentageByMonthNumber(11);
    }

    public Float getDecemberPercentage() {
        return getPercentageByMonthNumber(12);
    }
//#endregion
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
