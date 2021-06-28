package com.caramba.ordertool.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationListTest {

    @Test
    void sortRecommendationsByDate() {
        RecommendationList recommendationList = new RecommendationList();
        Supplier supplier = new Supplier("TestSupplier", 7);
        Random random = new Random();
        //add recommendations with randomized dates
        for(int i = 1; i <= 100; i++){
            YearMonth date = YearMonth.now().plusMonths(random.nextInt(120));
            recommendationList.addRecommendation(new Recommendation(supplier, date));
        }
        recommendationList.sortRecommendationsByDate();
        LocalDate previousFinalOrderDate = null;
        for (Recommendation recommendation : recommendationList.getRecommendations()) {
            if(previousFinalOrderDate != null){
                //recommendations should be sorted from new to old
                assertFalse(recommendation.getFinalOrderDate().isAfter(previousFinalOrderDate));
            }
            previousFinalOrderDate = recommendation.getFinalOrderDate();
        }
    }
}