package com.caramba.ordertool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderAlgorithm {
    TimePeriodController tpc = new TimePeriodController();
    ArrayList<TimePeriod> currentPeriod;
    TimePeriod nextPeriod;

    public OrderAlgorithm(){
        currentPeriod = tpc.getActiveTimePeriods();
        nextPeriod = null;
    }

    public HashMap<Product, Integer> createSuggestionList(HashMap<Product, Integer> productList){
        HashMap<Product, Integer> suggestionList = new HashMap<>();

        for (int i = 0; i < tpc.getTimePeriods().size(); i++) {
            for (int j = 0; j < currentPeriod.size(); j++) {
                if(tpc.getActiveTimePeriods().equals(currentPeriod.get(j) ) ){
                    for (Map.Entry<Product, Integer> entry : productList.entrySet()) {
                        
                    }
                }
            }
        }


        return suggestionList;
    }


}
