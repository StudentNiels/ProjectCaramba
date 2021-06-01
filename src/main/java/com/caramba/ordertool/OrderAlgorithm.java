package com.caramba.ordertool;

import java.lang.reflect.Array;
import java.time.LocalDate;
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

    // Hoeveel producten er besteld moeten worden
    public int getProductRecommendation(UUID productID){
        Saleslist salesList = Application.getMainSalesList().getSalesByProduct(productID);
        HashMap<LocalDate, Integer> dateAmountList = new HashMap<>();

        for(Sale sale : salesList.getSales()){
            dateAmountList.put(sale.getDate(), sale.getAmountByID(productID));
        }

        return 0;
    }

    /**
     * Analyzes the sales of the product in previous years to calculate a 'median year'.
     * The median year includes the median of products sold in a certain month and the median of the total sold per year
     * @param dateAmountList
     * @return
     */
    public int[] getMedianYear(HashMap<LocalDate, Integer> dateAmountList){
        int[] median = new int[13];
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
        ArrayList<Integer> totalAmount      = new ArrayList<>();

        for (Map.Entry<LocalDate, Integer> entry : dateAmountList.entrySet()) {
            switch (entry.getKey().getMonth()){
                case JANUARY    -> januaryAmount    .add(entry.getValue());
                case FEBRUARY   -> februaryAmount   .add(entry.getValue());
                case MARCH      -> marchAmount      .add(entry.getValue());
                case APRIL      -> aprilAmount      .add(entry.getValue());
                case MAY        -> mayAmount        .add(entry.getValue());
                case JUNE       -> juneAmount       .add(entry.getValue());
                case JULY       -> julyAmount       .add(entry.getValue());
                case AUGUST     -> augustAmount     .add(entry.getValue());
                case SEPTEMBER  -> septemberAmount  .add(entry.getValue());
                case OCTOBER    -> octoberAmount    .add(entry.getValue());
                case NOVEMBER   -> novemberAmount   .add(entry.getValue());
                case DECEMBER   -> decemberAmount   .add(entry.getValue());
            }
            if(yearTotal.containsKey(entry.getKey().getYear())){
                yearTotal.replace(entry.getKey().getYear(),(entry.getValue() + yearTotal.get(entry.getKey().getYear())));
            }
            else{
                yearTotal.put(entry.getKey().getYear(), entry.getValue());
            }
        }

        for (Map.Entry<Integer, Integer> entry : yearTotal.entrySet()) {
            totalAmount.add(entry.getValue());
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
        median[12] = getMedianFromArrayList(totalAmount);

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
            int centerIndex = arrayList.size() / 2;
            return (arrayList.get(centerIndex) + arrayList.get(centerIndex + 1)) / 2;
        }else{
            //odd
            return arrayList.get((int)Math.ceil(arrayList.size() / 2));
        }
    }
}
