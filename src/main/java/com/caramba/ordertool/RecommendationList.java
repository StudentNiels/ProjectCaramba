package com.caramba.ordertool;

import java.util.ArrayList;

public class RecommendationList {
    private ArrayList<Recommendation> recommendations;

    public RecommendationList() {
        this.recommendations = new ArrayList<>();
    }

    public void addRecommendation(Recommendation recommendation){
        recommendations.add(recommendation);
    }

    public ArrayList<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(ArrayList<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }
}
