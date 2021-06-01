package com.caramba.ordertool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderAlgorithm {
    TimePeriodController tpc = new TimePeriodController();
    ArrayList<TimePeriod> currentPeriod = tpc.getActiveTimePeriods();
    TimePeriod nextPeriod = null;

    /**
     * This method is the algorithm that will return a suggestion list with amounts
     * NOTE: this is only a suggestion, sales user will still have to either confirm the amount or enter their own
     *
     * magazijn capaciteit vragen
     * Kijken of inkoop mogelijkheid verminderd kan worden voor meer winst
     * mooie auto, maar geen motor, dus dat werkt niet
     *
     * Period - done
     * Beschikbare voorraad - done (staat in product)
     * Geschatte levertijd
     * verkoop trend
     *
     * @param productList A list containing the products that are to be filtered
     * @return suggestionList with products and amounts
     * */
    public HashMap<Product, Integer> createSuggestionList(HashMap<UUID, Product> productList){
        HashMap<Product, Integer> suggestionList = new HashMap<>();

        //Loop through all TimePeriods in TimePeriodController
        for (int i = 0; i < tpc.getTimePeriods().size(); i++) {
            //Loop through all current periods
            for (int j = 0; j < currentPeriod.size(); j++) {
                //check every current period with the active time period
                if(tpc.getActiveTimePeriods().equals(currentPeriod.get(j) ) ){
                    //Loop through all products to see which products match the current period
                    for (Map.Entry<UUID, Product> entry : productList.entrySet()) {
                        Product product = entry.getValue();
                        //Compare product period to current period
                        if(product.getPeriod().equals(currentPeriod.get(j))){
                            //If product has extremely low quantity remaining, add more to the order suggestion
                            if(product.getQuantity() <= (product.getMinQuantity() * 0.20)){
                                //TODO: appropriate functionality
                                /*
                                * 
                                *
                                * */
                            }// if product has low quantity remaining, suggest to minimumQuantity
                            else if(product.getQuantity() <= (product.getMinQuantity() * 0.40)){
                                //TODO: appropriate functionality
                            }// if product is empty, use sales numbers to determine how much should be bought
                            else if(product.getQuantity() == 0){
                                //TODO: appropriate functionality
                            }
                        }else{
                            //TODO: appropriate functionality (outside current period)
                        }
                    }
                }
            }
        }
        return suggestionList;
    }
}
