package com.caramba.ordertool.models;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a typical year of sales of a certain product
 */
public class MedianYear {
    private final int[] saleQuantities;

    /**
     * Private constructor for MedianYear
     *
     * @param saleQuantities int array representing the amount of units sold in a certain month. The month corresponds to the index of the array: i=0 is january, i=1 february ect.
     * @throws IllegalArgumentException if the array size is not 12
     */
    private MedianYear(int[] saleQuantities) throws IllegalArgumentException {
        if (saleQuantities.length != 12) {
            throw new IllegalArgumentException();
        }
        this.saleQuantities = saleQuantities;
    }

    /**
     * Analyzes the sales of the product in previous years to calculate a 'median year'.
     * The median year includes the median of products sold in per month of the year.
     *
     * @param dateAmountList hashmap with quantity sold in a certain YearMonth
     * @return array with the median of amount sold where i = the month of the year
     */
    public static MedianYear getMedianYear(HashMap<YearMonth, Integer> dateAmountList) {
        int[] median = new int[12];
        ArrayList<Integer> januaryAmount = new ArrayList<>();
        ArrayList<Integer> februaryAmount = new ArrayList<>();
        ArrayList<Integer> marchAmount = new ArrayList<>();
        ArrayList<Integer> aprilAmount = new ArrayList<>();
        ArrayList<Integer> mayAmount = new ArrayList<>();
        ArrayList<Integer> juneAmount = new ArrayList<>();
        ArrayList<Integer> julyAmount = new ArrayList<>();
        ArrayList<Integer> augustAmount = new ArrayList<>();
        ArrayList<Integer> septemberAmount = new ArrayList<>();
        ArrayList<Integer> octoberAmount = new ArrayList<>();
        ArrayList<Integer> novemberAmount = new ArrayList<>();
        ArrayList<Integer> decemberAmount = new ArrayList<>();
        for (Map.Entry<YearMonth, Integer> entry : dateAmountList.entrySet()) {
            switch (entry.getKey().getMonth()) {
                case JANUARY -> januaryAmount.add(entry.getValue());
                case FEBRUARY -> februaryAmount.add(entry.getValue());
                case MARCH -> marchAmount.add(entry.getValue());
                case APRIL -> aprilAmount.add(entry.getValue());
                case MAY -> mayAmount.add(entry.getValue());
                case JUNE -> juneAmount.add(entry.getValue());
                case JULY -> julyAmount.add(entry.getValue());
                case AUGUST -> augustAmount.add(entry.getValue());
                case SEPTEMBER -> septemberAmount.add(entry.getValue());
                case OCTOBER -> octoberAmount.add(entry.getValue());
                case NOVEMBER -> novemberAmount.add(entry.getValue());
                case DECEMBER -> decemberAmount.add(entry.getValue());
            }
        }
        median[0] = getMedianFromArrayList(januaryAmount);
        median[1] = getMedianFromArrayList(februaryAmount);
        median[2] = getMedianFromArrayList(marchAmount);
        median[3] = getMedianFromArrayList(aprilAmount);
        median[4] = getMedianFromArrayList(mayAmount);
        median[5] = getMedianFromArrayList(juneAmount);
        median[6] = getMedianFromArrayList(julyAmount);
        median[7] = getMedianFromArrayList(augustAmount);
        median[8] = getMedianFromArrayList(septemberAmount);
        median[9] = getMedianFromArrayList(octoberAmount);
        median[10] = getMedianFromArrayList(novemberAmount);
        median[11] = getMedianFromArrayList(decemberAmount);
        return new MedianYear(median);
    }

    /**
     * Calculates the median from an arraylist with integers. If there are an even amount of numbers, the average between the two middle values is returned.
     *
     * @param arrayList the numbers to get the median of
     * @return the median of the array
     */
    private static int getMedianFromArrayList(ArrayList<Integer> arrayList) {
        Collections.sort(arrayList);
        if (arrayList.size() == 0) {
            return 0;
        } else if (arrayList.size() == 1) {
            return arrayList.get(0);
        } else if ((arrayList.size() % 2) == 0) {
            //even
            int centerIndex = (arrayList.size() / 2) - 1;
            return (arrayList.get(centerIndex) + arrayList.get(centerIndex + 1)) / 2;
        } else {
            //odd
            return arrayList.get((int) Math.ceil((float) arrayList.size() / 2) - 1);
        }
    }

    /**
     * Getter for salesQuantity.
     *
     * @param monthNumber month number to get the sales quantity for ( 1 = january, ect.)
     * @return amount of units sold
     * @throws IllegalArgumentException if the specified month number is outside the 1 - 12 range
     */
    public int getByMonthNumber(int monthNumber) {
        if (monthNumber < 1 || monthNumber > 12) {
            throw new IllegalArgumentException();
        }
        return saleQuantities[monthNumber - 1];
    }

}
