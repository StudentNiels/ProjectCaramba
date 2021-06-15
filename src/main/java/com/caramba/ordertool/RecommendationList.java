package com.caramba.ordertool;

import java.util.ArrayList;

public class RecommendationList {
    private ArrayList<Recommendation> recommendationList;

    public RecommendationList() {
        this.recommendationList = new ArrayList<>();
    }

    public void addRecommendation(Recommendation recommendation){
        recommendationList.add(recommendation);
    }

    public ArrayList<Recommendation> getRecommendationList() {
        return recommendationList;
    }

    public void setRecommendationList(ArrayList<Recommendation> recommendationList) {
        this.recommendationList = recommendationList;
    }
}
