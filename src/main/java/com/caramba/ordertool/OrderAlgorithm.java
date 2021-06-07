package com.caramba.ordertool;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;

public class OrderAlgorithm {

    /**
     * Checks all registered products and checks how many should be ordered for the given month
     * @return hashmap with products and quantity to order
     */
    public HashMap<Product, Integer> createOrderList(YearMonth date){
        HashMap<Product, Integer> result = new HashMap<>();
        for (Map.Entry<String, Product> entry : Application.getMainProductList().getProducts().entrySet()) {
            String id = entry.getKey();
            Product p = entry.getValue();
            int amount =  RecommendOrderAmount(id, date);
            if(amount > 0){
                result.put(p, amount);
            }
        }
        return result;
    }

    /**
     * Calculates how many products to order based on current stock and expected sales
     * @param productID
     * @return
     */
    public int RecommendOrderAmount(String productID, YearMonth date){
        Product p = Application.getMainProductList().get(productID);
        int result = getProjectedSaleAmount(productID, date) - p.getQuantity();
        return Math.max(result, 0);
    }

    /**
     * Calculates how many units of a certain product is expected to be sold in the given month
     * based on the median of sales per month on record.
     * @param productID The uuid of the product to check
     * @param date The yearMonth to get the projected sales for. Must be in the future.
     * @return The amount to order
     */
    public int getProjectedSaleAmount(String productID, YearMonth date){
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
        //this is what we think an average year looks like in terms of sales.
        //e.g.: in january we typically sell 10 units, in february we typically sell 5, ect.
        //the current year is not taken into account for a reason mentioned below
        int[] medianYear = getMedianYear(dateAmountList);

        //The median year is calculated as percentages so we can show seasonal trends:
        //e.g.: 50% of our sales for this product typically happen in month of january, 25% in february, ect.
        float[] medianPercentages = calculatePercentages(medianYear);

        //If we have sold this product before in the current year, we extrapolate the future sales of this year based on the seasonal trends
        //We assume this is more accurate then simply using median directly, as the sales of the current year
        //will probably be better at reflecting the growth/shrinkage of sales in general and the median year is mostly meant to show seasonal trends.
        //This is also why the current year is not used in calculating the median year.
        float percentageSoldThisYear = 0;
        for(int i = 0; i < LocalDate.now().getMonth().getValue(); i++){
            //in a typical year, this many % of the units would be sold at this point in time. We will assume that this year will be the same.
            percentageSoldThisYear = percentageSoldThisYear + medianPercentages[i];
        }if(percentageSoldThisYear != 0 || date.getYear() != Year.now().getValue()){
            //Finally we calculate the amount we expect to sell based on the percentage and the amount of units actually sold.
            int totalExpectedToSellThisYear = Math.round(totalSoldThisYear / percentageSoldThisYear);
            return Math.round(totalExpectedToSellThisYear * medianPercentages[date.getMonth().getValue() - 1]);
        }else{
            //If we sold 0 so far then we don't have any data to extrapolate on, so we assume the amount sold will be equal to that of the median year.
            return medianYear[date.getMonth().getValue()];
        }

    }

    /**
     * @param productID product to get sales projection of.
     * @param amountOfMonths The amount of months to check
     * @return Array with the projected sales of the next x months
     */
    public int[] getProjectedSalesInComingMonths(String productID, int amountOfMonths){
        int[] result = new int[amountOfMonths];
        for(int i = 0; i < amountOfMonths; i++){
            result[i] = getProjectedSaleAmount(productID, YearMonth.now().plusMonths(1 + i));
        }
        return result;
    }

    public int getProjectedSalesNextMonth(String productID){
        return getProjectedSalesInComingMonths(productID, 1)[0];
    }

    /**
     * Analyzes the sales of the product in previous years to calculate a 'median year'.
     * The median year includes the median of products sold in per month of the year.
     * NOTE: does NOT use the sales during the current year, only previous years.
     * @param dateAmountList hashmap with quantity sold in a certain YearMonth
     * @return array with the median of amount sold where i = the month of the year
     */
    public int[] getMedianYear(HashMap<YearMonth, Integer> dateAmountList){
        int[] median = new int[12];
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
            return arrayList.get((int)Math.ceil((float) arrayList.size() / 2) - 1);
        }
    }

    /**
     * Calculates the distribution of values in percentages.
     * EXAMPLE
     * int[3, 5, 4] would return  int[36(%), 60(%), 48(%)]
     * @param array array to calculate of
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
