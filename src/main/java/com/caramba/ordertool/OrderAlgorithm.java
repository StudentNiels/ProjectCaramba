package com.caramba.ordertool;

import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class OrderAlgorithm {
    //TODO: make this stuff efficient code
    /**
     * This method is the algorithm that will return a suggestion list with amounts
     * NOTE: this is only a suggestion, sales user will still have to either confirm the amount or enter their own
     *
     * magazijn capaciteit vragen
     * Kijken of inkoop mogelijkheid verminderd kan worden voor meer winst
     * mooie auto, maar geen motor, dus dat werkt niet
     *
     * Period
     * Beschikbare voorraad
     * Geschatte levertijd
     * verkoop trend
     *
     * @param productList A list containing the products that are to be filtered
     * @return suggestionList with products and amounts
     * */
    public HashMap<Product, Integer> createSuggestionList(HashMap<UUID, Product> productList){
        HashMap<Product, Integer> suggestionList = new HashMap<>();

        // UUID matchen van Product en Saleslist


        return suggestionList;
    }

    /**
     * Calculates how many units of a certain product is expected to be sold in the given month
     * based on the median of sales per month on record.
     * @param productID The uuid of the product to check
     * @param date The yearmonth to get the projected sales for. Must be in the future. todo not implemented yet
     * @return The amount to order
     */
    public int getProjectedSales(UUID productID, YearMonth date){
        if(!date.isAfter(YearMonth.now())){
            throw new InvalidParameterException("The given date is not in the future");
        }
        Saleslist salesList = Application.getMainSalesList().getSalesByProduct(productID);
        HashMap<YearMonth, Integer> dateAmountList = new HashMap<>();
        int totalSoldThisYear = 0;
        for(Sale sale : salesList.getSales()){
            int amount = sale.getAmountByID(productID);
            YearMonth yearMonth = YearMonth.from(sale.getDate());
            if(dateAmountList.containsKey(yearMonth)){
                dateAmountList.put(yearMonth, amount + dateAmountList.get(yearMonth));
            }else{
                dateAmountList.put(yearMonth, amount);
            }
            if(sale.getDate().getYear() == LocalDate.now().getYear()){
                totalSoldThisYear = totalSoldThisYear + amount;
            }
        }
        float[] medianPercentages = calculatePercentages(getMedianYear(dateAmountList));
        float percentageSoldThisYear = 0;
        for(int i = 0; i < LocalDate.now().getMonth().getValue(); i++){
            percentageSoldThisYear = percentageSoldThisYear + medianPercentages[i];
        }
        int totalExpectedToSellThisYear = Math.round(totalSoldThisYear / percentageSoldThisYear);
        return Math.round(totalExpectedToSellThisYear * medianPercentages[LocalDate.now().plusMonths(1).getMonth().getValue()]);
    }

    /**
     * @param productID product to get sales projection of.
     * @param amountOfMonths The amount of months to check
     * @return Array with the projected sales of the next x months
     */
    public int[] getProjectedSalesInComingMonths(UUID productID, int amountOfMonths){
        int[] result = new int[amountOfMonths];
        for(int i = 0; i < amountOfMonths; i++){
            result[i] = getProjectedSales(productID, YearMonth.now().plusMonths(1 + i));
        }
        return result;
    }

    public int getProjectedSalesNextMonth(UUID productID){
        return getProjectedSalesInComingMonths(productID, 1)[0];
    }

    /**
     * Analyzes the sales of the product in previous years to calculate a 'median year'.
     * The median year includes the median of products sold in per month of the year.
     * @param dateAmountList
     * @return
     */
    public int[] getMedianYear(HashMap<YearMonth, Integer> dateAmountList){
        int[] median = new int[12];
        HashMap<Integer, Integer> yearTotal = new HashMap<>();
        ArrayList<Integer> januaryAmount    = new ArrayList<>();
        ArrayList<Integer> februaryAmount   = new ArrayList<>();
        ArrayList<Integer> marchAmount      = new ArrayList<>();
        ArrayList<Integer> aprilAmount      = new ArrayList<>();
        ArrayList<Integer> mayAmount        = new ArrayList<>();
        ArrayList<Integer> juneAmount       = new ArrayList<>();
        ArrayList<Integer> julyAmount       = new ArrayList<>();
        ArrayList<Integer> augustAmount     = new ArrayList<>();
        ArrayList<Integer> septemberAmount  = new ArrayList<>();
        ArrayList<Integer> octoberAmount    = new ArrayList<>();
        ArrayList<Integer> novemberAmount   = new ArrayList<>();
        ArrayList<Integer> decemberAmount   = new ArrayList<>();
        for (Map.Entry<YearMonth, Integer> entry : dateAmountList.entrySet()) {
            if(entry.getKey().getYear() != LocalDate.now().getYear()) {
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
        return median;
    }

    public int getMedianFromArrayList(ArrayList<Integer> arrayList){
        Collections.sort(arrayList);
        if(arrayList.size() == 0){
            return 0;
        }else if(arrayList.size() == 1){
            return arrayList.get(0);
        }else if((arrayList.size() % 2) == 0){
            //even
            int centerIndex = (arrayList.size() / 2) - 1;
            return (arrayList.get(centerIndex) + arrayList.get(centerIndex + 1)) / 2;
        }else{
            //odd
            return arrayList.get((int)Math.ceil(arrayList.size() / 2) - 1);
        }
    }

    /**
     * Calculates the distribution of values in percentages.
     * EXAMPLE
     * int[3, 5, 4] would return  int[36(%), 60(%), 48(%)]
     * @param array
     * @return array with percentages
     */
    private float[] calculatePercentages(int[] array){
        float[] result = new float[array.length];
        int total = Arrays.stream(array).sum();
        for(int i = 0; i < array.length; i++){
            result[i] = (float) array[i] / total;
        }
        return result;
    }
}
